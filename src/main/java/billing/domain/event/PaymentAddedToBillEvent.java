package billing.domain.event;

import billing.domain.Reference;
import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;

public class PaymentAddedToBillEvent extends Event implements IMessage {

  public static final String NAME = "payment_added_to_bill";
  private UUID paymentId;
  private Reference billReference;
  private UUID merchantId;
  private DateTime occurredAt;
  private Integer version = 1;

  public PaymentAddedToBillEvent(UUID paymentId, Reference billReference, UUID merchantId) {
    this.paymentId = paymentId;
    this.billReference = billReference;
    this.merchantId = merchantId;
    this.occurredAt = new DateTime();
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("billReference", this.billReference.toString());
    payload.put("id", this.paymentId.toString());
    payload.put("merchantId", this.merchantId.toString());
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
