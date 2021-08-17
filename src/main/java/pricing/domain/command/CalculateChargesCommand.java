package pricing.domain.command;

import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;

public class CalculateChargesCommand extends Command implements IMessage {

  public static final String NAME = "calculate_charges";
  private UUID paymentId;
  private Money value;
  private Money interchangeCost;
  private Money schemeFee;
  private UUID merchantId;
  private DateTime occurredAt;
  private Integer version = 1;

  public CalculateChargesCommand(UUID paymentId, Money value, Money interchangeCost,
      Money schemeFee, UUID merchantId) {
    this.paymentId = paymentId;
    this.value = value;
    this.interchangeCost = interchangeCost;
    this.schemeFee = schemeFee;
    this.merchantId = merchantId;
    this.occurredAt = DateTime.now();
  }

  public Money getValue() {
    return value;
  }

  public Money getInterchangeCost() {
    return interchangeCost;
  }

  public Money getSchemeFee() {
    return schemeFee;
  }

  public UUID getMerchantId() {
    return merchantId;
  }

  public static CalculateChargesCommand fromMessage(IMessage message) {
    if (!message.getName().matches(CalculateChargesCommand.NAME)) {
      throw new RuntimeException();
    }
    Map<String, String> payload = message.getPayload();
    return new CalculateChargesCommand(
        UUID.fromString(payload.get("id")),
        new Money(
            Integer.valueOf(payload.get("value")),
            Currency.valueOf(payload.get("currency"))
        ),
        new Money(
            Integer.valueOf(payload.get("interchangeCostAmount")),
            Currency.valueOf(payload.get("interchangeCostCurrency"))
        ),
        new Money(
            Integer.valueOf(payload.get("schemeFeeAmount")),
            Currency.valueOf(payload.get("schemeFeeCurrency"))
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

  public UUID getPaymentId() {
    return paymentId;
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
