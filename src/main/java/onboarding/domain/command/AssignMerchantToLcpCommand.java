package onboarding.domain.command;

import billing.domain.command.Command;
import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import money.Currency;
import money.Money;
import onboarding.domain.Lcp;
import org.joda.time.DateTime;

public class AssignMerchantToLcpCommand extends Command implements IMessage {

  public static final String NAME = "assign_merchant_to_lcp";
  private UUID merchantId;
  private Lcp lcp;
  private DateTime occurredAt;
  private Integer version = 1;

  public AssignMerchantToLcpCommand(UUID merchantId, Lcp lcp) {
    this.merchantId = merchantId;
    this.lcp = lcp;
    this.occurredAt = new DateTime();
  }

  public static AssignMerchantToLcpCommand fromMessage(IMessage message) {
    if (!message.getName().matches(AssignMerchantToLcpCommand.NAME)) {
      throw new RuntimeException(
          "POC999 - Incompatible event: '"
              + message.getName()
              + "' cannot be converted to '"
              + NAME
              + "'");
    }
    Map<String, String> payload = message.getPayload();
    return new AssignMerchantToLcpCommand(
        UUID.fromString(payload.get("id")), new Lcp(payload.get("lcp").toString()));
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("id", this.merchantId.toString());
    payload.put("lcp", this.lcp.toString());
    return payload;
  }

  public UUID getMerchantId() {
    return merchantId;
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
