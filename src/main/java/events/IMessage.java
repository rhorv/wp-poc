package events;

import java.util.Map;
import org.joda.time.DateTime;

public interface IMessage {

  public String getName();

  public Map<String, String> getPayload();

  public Integer getVersion();

  public DateTime getOccurredAt();

  public String getCategory();
}
