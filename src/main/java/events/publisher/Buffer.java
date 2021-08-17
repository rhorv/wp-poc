package events.publisher;

import events.IMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Buffer implements IPublish {

  private List<IMessage> messages = new ArrayList<IMessage>();
  private IPublish publisher;

  public Buffer(IPublish publisher) {
    this.publisher = publisher;
  }

  public void publish(IMessage message) {
    messages.add(message);
  }

  public void flush() throws Exception {
    ListIterator<IMessage> iterator = this.messages.listIterator();
    while (iterator.hasNext()) {
      this.publisher.publish(iterator.next());
      iterator.remove();
    }
  }
}
