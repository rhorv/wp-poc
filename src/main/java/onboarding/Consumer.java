package onboarding;

import events.consumer.IConsume;
import events.dispatcher.IDispatch;
import events.dispatcher.IHandle;
import events.publisher.Buffer;
import java.util.UUID;
import onboarding.domain.Lcp;
import onboarding.domain.command.AssignMerchantToLcpCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Consumer {

  public void start() {

    ApplicationContext appContext =
        new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    IConsume consumer = (IConsume) appContext.getBean("onboarding.messageConsumer");
    IDispatch dispatcher = (IDispatch) appContext.getBean("dispatcher");
    Buffer bufferPublisher = (Buffer) appContext.getBean("onboarding.bufferPublisher");

    onboarding.service.DomainEvent.registerPublisher(bufferPublisher);

    dispatcher.subscribe(
        "assign_merchant_to_lcp",
        (IHandle) appContext.getBean("assignMerchantToLcpCommandHandler"));

//    bufferPublisher.publish(
//        new AssignMerchantToLcpCommand(
//            UUID.fromString("27d2aadb-c444-4788-9195-5a42715a1d13"), new Lcp("WP-UK")));

    try {
//      bufferPublisher.flush();
      consumer.consume();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
