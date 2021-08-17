package events.publisher;

import events.IMessage;
import java.util.Map;

public class SplitPublisher implements IPublish {

  private Map<String, IPublish> publishers;

  public SplitPublisher(Map<String, IPublish> publishers) {
    this.publishers = publishers;
  }

  public void publish(IMessage message) throws Exception {
    IPublish publisher = publishers.get(message.getCategory());
    publisher.publish(message);
  }
}
