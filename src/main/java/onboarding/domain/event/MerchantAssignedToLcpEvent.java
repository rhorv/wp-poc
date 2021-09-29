package onboarding.domain.event;

import billing.domain.Reference;
import billing.domain.event.Event;
import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import onboarding.domain.Lcp;
import org.joda.time.DateTime;

public class MerchantAssignedToLcpEvent extends Event implements IMessage {

  public static final String NAME = "merchant_assigned_to_lcp";
  private UUID id;
  private Lcp lcp;
  private DateTime occurredAt;
  private Integer version = 1;

  public MerchantAssignedToLcpEvent(UUID id, Lcp lcp) {
    this.id = id;
    this.lcp = lcp;
    this.occurredAt = new DateTime();
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("id", this.id.toString());
    payload.put("lcp", this.lcp.toString());
    return payload;
  }

  public UUID getId() {
    return id;
  }

  public Lcp getLcp() {
    return lcp;
  }

  public String getName() {
    return NAME;
  }

  public DateTime getOccurredAt() {
    return this.occurredAt;
  }

  public Integer getVersion() {
    return this.version;
  }
}
