package poc;

import clearing.domain.Payment;
import clearing.domain.Scheme;
import clearing.domain.command.ClearPaymentCommand;
import clearing.service.infrastructure.InMemoryPaymentRepository;
import events.consumer.IConsume;
import events.dispatcher.IDispatch;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import events.publisher.IPublish;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pricing.domain.BlendedTariff;
import pricing.domain.Merchant;
import pricing.domain.PassThroughTariff;
import pricing.service.infrastructure.InMemoryMerchantRepository;


public class MyApplication {

  public static void main(String[] args) {

    ApplicationContext appContext =
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    IPublish publisher = (IPublish) appContext.getBean("messagePublisher");
    InMemoryPaymentRepository paymentRepository = (InMemoryPaymentRepository) appContext
        .getBean("paymentRepository");
    Payment payment = new Payment(Scheme.VISA, new Money(50000, Currency.GBP), DateTime.now(),
        UUID.randomUUID());

    InMemoryMerchantRepository merchantRepository = (InMemoryMerchantRepository) appContext
        .getBean("merchantRepository");
    Merchant merchant = new Merchant(payment.getMerchantId(),
        new PassThroughTariff(1));
    merchantRepository.save(merchant);

    try {
      paymentRepository.save(payment);
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      publisher.publish(
          new ClearPaymentCommand(payment.getId())
      );
    } catch (Exception e) {
      e.printStackTrace();
    }

    IConsume consumer = (IConsume) appContext.getBean("messageConsumer");
    IDispatch dispatcher = (IDispatch) appContext.getBean("dispatcher");
    Buffer bufferPublisher = (Buffer) appContext.getBean("bufferPublisher");

    clearing.service.DomainEvent.registerPublisher(bufferPublisher);
    pricing.service.DomainEvent.registerPublisher(bufferPublisher);

    dispatcher.subscribe(
        "clear_payment", (IHandle) appContext.getBean("clearPaymentCommandHandler"));
    dispatcher.subscribe(
        "payment_cleared", (IHandle) appContext.getBean("paymentClearedEventHandler"));
    dispatcher.subscribe(
        "calculate_charges", (IHandle) appContext.getBean("calculateChargesCommandHandler"));

    try {
      consumer.consume();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
