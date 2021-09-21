package funding.domain.event;

import events.IMessage;
import funding.domain.Reference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;

public class PaymentAddedToFundingBalanceEvent extends Event implements IMessage {

  public static final String NAME = "payment_added_to_funding_balance";
  private UUID paymentId;
  private Reference fundingBalanceReference;
  private UUID merchantId;
  private DateTime occurredAt;
  private Integer version = 1;

  public PaymentAddedToFundingBalanceEvent(
      UUID paymentId, Reference fundingBalanceReference, UUID merchantId) {
    this.paymentId = paymentId;
    this.fundingBalanceReference = fundingBalanceReference;
    this.merchantId = merchantId;
    this.occurredAt = new DateTime();
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("fundingBalanceReference", this.fundingBalanceReference.toString());
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
