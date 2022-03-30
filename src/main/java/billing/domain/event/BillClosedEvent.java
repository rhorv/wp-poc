package billing.domain.event;

import billing.domain.Reference;
import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import money.Money;
import org.joda.time.DateTime;

public class BillClosedEvent extends Event implements IMessage {

  public static final String NAME = "bill_closed";
  private UUID merchantId;
  private Reference reference;
  private Money totalCharge;
  private DateTime occurredAt;
  private Integer version = 1;

  public BillClosedEvent(UUID merchantId, Reference reference, Money totalCharge) {
    this.merchantId = merchantId;
    this.reference = reference;
    this.totalCharge = totalCharge;
    this.occurredAt = DateTime.now();
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("reference", this.reference.toString());
    payload.put("merchantId", this.merchantId.toString());
    payload.put("totalChargeCurrency", this.totalCharge.getCurrency().toString());
    payload.put("totalChargeAmount", this.totalCharge.getAmount().toString());
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
