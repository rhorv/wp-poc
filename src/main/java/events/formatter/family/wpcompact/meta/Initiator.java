package events.formatter.family.wpcompact.meta;

import java.util.HashMap;
import java.util.Map;

public class Initiator extends MetaItem {

  private enum Source {
    message,
    api,
    scheduled_task
  }

  public static Initiator message(Map<String, String> details) {
    return new Initiator(Source.message.toString(), details);
  }

  public static Initiator api(Map<String, String> details) {
    return new Initiator(Source.api.toString(), details);
  }

  public static Initiator scheduledTask(Map<String, String> details) {
    return new Initiator(Source.scheduled_task.toString(), details);
  }

  public Initiator(String name) {
    super(name, new HashMap<String, String>());
  }

  public Initiator(String name, Map<String, String> details) {
    super(name, details);
    Source source = Source.valueOf(name);
  }
}
