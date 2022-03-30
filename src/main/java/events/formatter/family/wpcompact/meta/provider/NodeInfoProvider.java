package events.formatter.family.wpcompact.meta.provider;

import events.formatter.family.wpcompact.meta.MetaItem;
import events.formatter.family.wpcompact.meta.MetaItemProvider;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class NodeInfoProvider implements MetaItemProvider {

  public MetaItem get() throws Exception {
    Map<String, String> details = new HashMap<>();
    details.put("ip", InetAddress.getLocalHost().toString());
    return new MetaItem("localNode", details);
  }
}
