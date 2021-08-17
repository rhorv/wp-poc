package events.dispatcher.guava;

import com.google.common.eventbus.Subscribe;
import events.IMessage;
import events.dispatcher.IHandle;

public class GuavaListener {

  private IHandle handler;

  public GuavaListener(IHandle handler) {
    this.handler = handler;
  }

  @Subscribe
  public void handle(IMessage message) throws Exception {
    handler.handle(message);
  }

}
