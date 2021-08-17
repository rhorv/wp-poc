package events.formatter;

import events.IMessage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public interface ISerializeMessage {

  public Envelope serialize(IMessage message) throws Exception;
}
