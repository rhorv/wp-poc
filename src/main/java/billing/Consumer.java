package billing;

import events.consumer.IConsume;
import events.dispatcher.IDispatch;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {

  public void start() {

    ApplicationContext appContext =
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    IConsume consumer = (IConsume) appContext.getBean("billing.messageConsumer");
    IDispatch dispatcher = (IDispatch) appContext.getBean("dispatcher");
    Buffer bufferPublisher = (Buffer) appContext.getBean("billing.bufferPublisher");

    billing.service.DomainEvent.registerPublisher(bufferPublisher);

    dispatcher.subscribe(
        "payment_charge_calculated",
        (IHandle) appContext.getBean("paymentChargeCalculatedEventHandler"));
    dispatcher.subscribe(
        "add_payment_to_bill", (IHandle) appContext.getBean("addToBillCommandHandler"));

    try {
      consumer.consume();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
