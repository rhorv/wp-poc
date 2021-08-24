package clearing;

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

    IConsume consumer = (IConsume) appContext.getBean("clearing.messageConsumer");
    IDispatch dispatcher = (IDispatch) appContext.getBean("dispatcher");
    Buffer bufferPublisher = (Buffer) appContext.getBean("clearing.bufferPublisher");

    clearing.service.DomainEvent.registerPublisher(bufferPublisher);
    pricing.service.DomainEvent.registerPublisher(bufferPublisher);

    dispatcher.subscribe(
        "clear_payment", (IHandle) appContext.getBean("clearPaymentCommandHandler"));

    try {
      consumer.consume();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
