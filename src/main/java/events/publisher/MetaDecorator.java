package events.publisher;

import events.IMessage;
import events.Message;
import events.MetaItem;
import events.formatter.Envelope;
import events.formatter.ISerializeMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaDecorator implements ISerializeMessage {

  private ISerializeMessage serializer;
  private List<IProvideMeta> metaProviders = new ArrayList<>();

  public MetaDecorator(ISerializeMessage serializer,
      List<IProvideMeta> metaProviders) {
    this.serializer = serializer;
    this.metaProviders = metaProviders;
  }

  @Override
  public Envelope serialize(IMessage message) throws Exception {
    return null;
  }

  public Envelope serialize(Message message) throws Exception {
    Map<String, MetaItem> metaItems = new HashMap<>();
    for (IProvideMeta provider : this.metaProviders) {
      metaItems.put(provider.getName(), provider.getMetaFor(message));
    }
    /*
    Message decoratedMessage = new Message(message.getName(), message.getPayload(),
        message.getVersion(), message.getOccurredAt(), message.getCategory(), metaItems);
    return this.serializer.serialize(decoratedMessage);
     */
    return this.serializer.serialize(message);
  }
}
