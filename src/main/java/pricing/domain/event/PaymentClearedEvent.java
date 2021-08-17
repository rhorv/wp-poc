package pricing.domain.event;

import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;

public class PaymentClearedEvent extends Event implements IMessage {

  public static final String NAME = "payment_cleared";
  private UUID paymentId;
  private Money interchangeCost;
  private Money schemeFee;
  private Money value;
  private UUID merchantId;
  private DateTime occurredAt;
  private Integer version = 1;

  public PaymentClearedEvent(UUID paymentId, Money interchangeCost, Money schemeFee,
      Money value, UUID merchantId) {
    this.paymentId = paymentId;
    this.merchantId = merchantId;
    this.interchangeCost = interchangeCost;
    this.schemeFee = schemeFee;
    this.value = value;
    this.occurredAt = DateTime.now();
  }

  public UUID getPaymentId() {
    return paymentId;
  }

  public Money getInterchangeCost() {
    return interchangeCost;
  }

  public Money getSchemeFee() {
    return schemeFee;
  }

  public Money getValue() {
    return value;
  }

  public UUID getMerchantId() {
    return merchantId;
  }

  public static PaymentClearedEvent fromMessage(IMessage message) {
    if (!message.getName().matches(PaymentClearedEvent.NAME)) {
      throw new RuntimeException();
    }
    Map<String, String> payload = message.getPayload();
    return new PaymentClearedEvent(
        UUID.fromString(payload.get("id")),
        new Money(
            Integer.valueOf(payload.get("interchangeCostAmount")),
            Currency.valueOf(payload.get("interchangeCostCurrency"))
        ),
        new Money(
            Integer.valueOf(payload.get("schemeFeeAmount")),
            Currency.valueOf(payload.get("schemeFeeCurrency"))
        ),
        new Money(
            Integer.valueOf(payload.get("value")),
            Currency.valueOf(payload.get("currency"))
        ),
        UUID.fromString(payload.get("merchantId"))
    );
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("id", this.paymentId.toString());
    payload.put("interchangeCostAmount", this.interchangeCost.getAmount().toString());
    payload.put("interchangeCostCurrency", this.interchangeCost.getCurrency().toString());
    payload.put("schemeFeeAmount", this.schemeFee.getAmount().toString());
    payload.put("schemeFeeCurrency", this.schemeFee.getCurrency().toString());
    payload.put("value", this.value.getAmount().toString());
    payload.put("currency", this.value.getCurrency().toString());
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
