package events.formatter.family.wpcompact.meta;

import java.util.HashMap;
import java.util.Map;

public class ConsequenceOf extends MetaItem {

  private enum Source {
    message,
    api,
    scheduled_task
  }

  public static ConsequenceOf message(Map<String, String> details) {
    return new ConsequenceOf(ConsequenceOf.Source.message.toString(), details);
  }

  public static ConsequenceOf api(Map<String, String> details) {
    return new ConsequenceOf(ConsequenceOf.Source.api.toString(), details);
  }

  public static ConsequenceOf scheduledTask(Map<String, String> details) {
    return new ConsequenceOf(ConsequenceOf.Source.scheduled_task.toString(), details);
  }

  public ConsequenceOf(String name) {
    super(name, new HashMap<String, String>());
  }

  public ConsequenceOf(String name, Map<String, String> details) {
    super(name, details);
    ConsequenceOf.Source source = ConsequenceOf.Source.valueOf(name);
  }
}
