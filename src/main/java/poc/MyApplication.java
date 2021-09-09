package poc;

import clearing.domain.command.ClearPaymentCommand;
import events.publisher.IPublish;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyApplication {

  public static void main(String[] args) throws Exception {

    if (args.length == 0) {
      throw new Exception("no app name specified");
    }

    if (args[0].equals("clearing")) {
      System.out.println("Launching clearing application ...");
      clearing.Consumer consumer = new clearing.Consumer();
      consumer.start();
    } else if (args[0].equals("pricing")) {
      System.out.println("Launching pricing application ...");
      pricing.Consumer consumer = new pricing.Consumer();
      consumer.start();
    } else if (args[0].equals("billing")) {
      System.out.println("Launching billing application ...");
      billing.Consumer consumer = new billing.Consumer();
      consumer.start();
    } else if (args[0].equals("publish")) {
      System.out.println("Publishing initial clearing message ...");
      ApplicationContext appContext =
          new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

      IPublish publisher = (IPublish) appContext.getBean("clearing.messagePublisher");
      try {
        publisher.publish(
            new ClearPaymentCommand(UUID.fromString(args[1]))
        );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      throw new Exception("'" + args[0] + "' is not a valid app name");
    }
  }

}
