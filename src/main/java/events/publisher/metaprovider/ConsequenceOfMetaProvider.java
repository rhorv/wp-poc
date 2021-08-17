package events.publisher.metaprovider;

import events.IMessage;
import events.MetaItem;
import events.publisher.IProvideMeta;
import java.util.HashMap;
import java.util.Map;

public class ConsequenceOfMetaProvider implements IProvideMeta {

  public String getName() {
    return "ConsequenceOf";
  }

  public MetaItem getMetaFor(IMessage message) {
    Map<String, String> details = new HashMap<>();
    details.put("test", "value1");
    return new MetaItem("test", details);
  }
}
