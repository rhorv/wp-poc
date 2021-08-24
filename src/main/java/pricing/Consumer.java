package pricing;

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

    IConsume consumer = (IConsume) appContext.getBean("pricing.messageConsumer");
    IDispatch dispatcher = (IDispatch) appContext.getBean("dispatcher");
    Buffer bufferPublisher = (Buffer) appContext.getBean("pricing.bufferPublisher");

    clearing.service.DomainEvent.registerPublisher(bufferPublisher);
    pricing.service.DomainEvent.registerPublisher(bufferPublisher);

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
