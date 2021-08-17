package events.dispatcher.guava;

import events.Message;
import java.util.Map;
import org.joda.time.DateTime;

public class GuavaMessage extends Message {

  private Throwable exception = null;

  public GuavaMessage(String name, Map<String, String> payload, Integer version,
      DateTime occurredAt, String category) {
    super(name, payload, version, occurredAt, category);
  }

  public Throwable getException() {
    return exception;
  }

  public void setException(Throwable exception) {
    this.exception = exception;
  }
}
