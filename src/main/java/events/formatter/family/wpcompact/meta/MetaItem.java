package events.formatter.family.wpcompact.meta;

import java.util.HashMap;
import java.util.Map;

public class MetaItem {
  protected String name;
  protected Map<String, String> details;

  public MetaItem(String name) {
    this(name, new HashMap<>());
  }

  public MetaItem(String name, Map<String, String> details) {
    this.name = name;
    this.details = details;
  }

  public String getName() {
    return name;
  }

  public Map<String, String> getDetails() {
    return details;
  }
}
