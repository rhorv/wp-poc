package events.dispatcher.guava;

import com.google.common.eventbus.EventBus;
import events.IMessage;
import events.dispatcher.IDispatch;
import events.dispatcher.IHandle;
import java.util.HashMap;
import java.util.Map;

public class GuavaDispatcher implements IDispatch {

  private Map<String, EventBus> bus = new HashMap<String, EventBus>();

  public void subscribe(String messageName, IHandle handler) {
    if (!bus.containsKey(messageName)) {
      bus.put(messageName, new EventBus(new ExceptionHandler()));
    }

    EventBus channel = bus.get(messageName);
    channel.register(new GuavaListener(handler));
  }

  public void dispatch(IMessage message) throws Exception {
    if (!bus.containsKey(message.getName())) {
      return;
    }

    GuavaMessage guavaMessage =
        new GuavaMessage(
            message.getName(),
            message.getPayload(),
            message.getVersion(),
            message.getOccurredAt(),
            message.getCategory());

    EventBus channel = bus.get(message.getName());
    channel.post(guavaMessage);
    if (guavaMessage.getException() != null) {
      throw new Exception(guavaMessage.getException());
    }
  }
}
