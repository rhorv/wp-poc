package events.publisher;

import events.IMessage;
import events.MetaItem;

public interface IProvideMeta {

  public String getName();

  public MetaItem getMetaFor(IMessage message);
}
