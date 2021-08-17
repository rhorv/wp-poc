package events.dispatcher;

import events.IMessage;

public interface IDispatch {

  public void dispatch(IMessage message) throws Exception;

  public void subscribe(String messageName, IHandle handler);
}
