package funding;

import events.consumer.IConsume;
import events.dispatcher.IDispatch;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import funding.service.DomainEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {

  public void start() {

    ApplicationContext appContext =
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    IConsume consumer = (IConsume) appContext.getBean("funding.messageConsumer");
    IDispatch dispatcher = (IDispatch) appContext.getBean("dispatcher");
    Buffer bufferPublisher = (Buffer) appContext.getBean("funding.bufferPublisher");

    DomainEvent.registerPublisher(bufferPublisher);

    dispatcher.subscribe(
        "payment_cleared", (IHandle) appContext.getBean("funding.paymentClearedEventHandler"));
    dispatcher.subscribe(
        "add_payment_to_funding_balance",
        (IHandle) appContext.getBean("addToFundingBalanceCommandHandler"));

    try {
      consumer.consume();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
