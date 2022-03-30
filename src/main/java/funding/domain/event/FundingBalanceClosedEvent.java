package funding.domain.event;

import events.IMessage;
import funding.domain.Reference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import money.Money;
import org.joda.time.DateTime;

public class FundingBalanceClosedEvent extends Event implements IMessage {

  public static final String NAME = "funding_balance_closed";
  private UUID merchantId;
  private Reference reference;
  private Money totalBalance;
  private DateTime occurredAt;
  private Integer version = 1;

  public FundingBalanceClosedEvent(UUID merchantId, Reference reference, Money totalBalance) {
    this.merchantId = merchantId;
    this.reference = reference;
    this.totalBalance = totalBalance;
    this.occurredAt = DateTime.now();
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("reference", this.reference.toString());
    payload.put("merchantId", this.merchantId.toString());
    payload.put("totalBalanceAmount", this.totalBalance.getAmount().toString());
    payload.put("totalBalanceCurrency", this.totalBalance.getCurrency().toString());
    return payload;
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
