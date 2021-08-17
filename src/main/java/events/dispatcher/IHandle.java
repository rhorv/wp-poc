package events.dispatcher;

import events.IMessage;

public interface IHandle {

  public void handle(IMessage message) throws Exception;
}
