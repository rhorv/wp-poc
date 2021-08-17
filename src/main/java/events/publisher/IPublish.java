package events.publisher;

import events.IMessage;

public interface IPublish {

  public void publish(IMessage message) throws Exception;
}
