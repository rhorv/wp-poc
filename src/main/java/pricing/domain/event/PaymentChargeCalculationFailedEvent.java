package pricing.domain.event;

import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;

public class PaymentChargeCalculationFailedEvent extends Event implements IMessage {

  public static final String NAME = "payment_charge_calculation_failed";
  private UUID paymentId;
  private DateTime occurredAt;
  private String reason;
  private Integer version = 1;

  public PaymentChargeCalculationFailedEvent(UUID paymentId, String reason) {
    this.paymentId = paymentId;
    this.reason = reason;
    this.occurredAt = DateTime.now();
  }

  public UUID getPaymentId() {
    return paymentId;
  }

  public String getReason() {
    return reason;
  }

  public static PaymentChargeCalculationFailedEvent fromMessage(IMessage message) {
    if (!message.getName().matches(PaymentChargeCalculationFailedEvent.NAME)) {
      throw new RuntimeException();
    }
    Map<String, String> payload = message.getPayload();
    return new PaymentChargeCalculationFailedEvent(
        UUID.fromString(payload.get("id")),
        payload.get("reason")
    );
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("id", this.paymentId.toString());
    payload.put("reason", this.reason);
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
