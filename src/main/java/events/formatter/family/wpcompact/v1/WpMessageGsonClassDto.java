package events.formatter.family.wpcompact.v1;

import events.formatter.family.wpcompact.meta.WpMeta;
import java.util.HashMap;
import java.util.Map;

public class WpMessageGsonClassDto {

  public String id;
  public String name;
  public Map<String, String> payload = new HashMap<String, String>();
  public Integer version;
  public String occurredAt;
  public String category;
  public WpMessageMetaGsonClassDto meta;
}
