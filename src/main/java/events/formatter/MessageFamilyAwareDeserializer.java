package events.formatter;

import events.IMessage;
import java.util.HashMap;
import java.util.Map;

public class MessageFamilyAwareDeserializer implements IDeserializeMessage {

  private Map<String, IDeserializeMessage> deserializers = new HashMap<>();

  public MessageFamilyAwareDeserializer(
      Map<String, IDeserializeMessage> deserializers) {
    this.deserializers = deserializers;
  }

  public IMessage deserialize(Envelope envelope) throws Exception {
    if (!this.deserializers.containsKey(envelope.getHeader().get("nameSerde"))) {
      throw new Exception();
    }
    return this.deserializers.get(envelope.getHeader().get("nameSerde")).deserialize(envelope);
  }
}
