package events;

import java.util.HashMap;
import java.util.Map;

public class MetaItem {

  private String name;
  private Map<String, String> details = new HashMap<>();

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
