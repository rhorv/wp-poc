package events.formatter;

import events.IMessage;
import java.util.HashMap;
import java.util.Map;

public class MessageFamilyAwareDeserializer implements IDeserializeMessage {

  private Map<String, IDeserializeMessage> deserializers = new HashMap<>();
  private String contentTypeKey;

  public MessageFamilyAwareDeserializer(
      String contentTypeKey, Map<String, IDeserializeMessage> deserializers) {
    this.contentTypeKey = contentTypeKey;
    this.deserializers = deserializers;
  }

  public IMessage deserialize(Envelope envelope) throws Exception {
    if (!this.deserializers.containsKey(envelope.getHeader().get(contentTypeKey))) {
      throw new Exception();
    }
    return this.deserializers.get(envelope.getHeader().get(contentTypeKey)).deserialize(envelope);
  }
}
