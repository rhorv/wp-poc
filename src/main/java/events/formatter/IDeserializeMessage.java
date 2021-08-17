package events.formatter;

import events.IMessage;
import java.io.ByteArrayInputStream;

public interface IDeserializeMessage {

  public IMessage deserialize(Envelope envelope) throws Exception;
}
