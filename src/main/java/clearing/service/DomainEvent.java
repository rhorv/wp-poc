package clearing.service;

import events.IMessage;
import events.publisher.IPublish;

public class DomainEvent {

  private static IPublish dispatcher;

  public static void registerPublisher(IPublish publisher) {
    dispatcher = publisher;
  }

  public static void publish(IMessage message) throws Exception {
    dispatcher.publish(message);
  }
}
