package billing.domain.command;

import billing.domain.Reference;
import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;

public class AddPaymentToBillCommand extends Command implements IMessage {

  public static final String NAME = "add_payment_to_bill";
  private UUID id;
  private UUID merchantId;
  private DateTime occurredAt;
  private Money charges;
  private Integer version = 1;

  public AddPaymentToBillCommand(UUID id, UUID merchantId, Money charges) {
    this.id = id;
    this.merchantId = merchantId;
    this.charges = charges;
    this.occurredAt = new DateTime();
  }

  public static AddPaymentToBillCommand fromMessage(IMessage message) {
    if (!message.getName().matches(AddPaymentToBillCommand.NAME)) {
      throw new RuntimeException(
          "POC999 - Incompatible event: '"
              + message.getName()
              + "' cannot be converted to '"
              + NAME
              + "'");
    }
    Map<String, String> payload = message.getPayload();
    return new AddPaymentToBillCommand(
        UUID.fromString(message.getPayload().get("id")),
        UUID.fromString(message.getPayload().get("merchantId")),
        new Money(
            Integer.valueOf(message.getPayload().get("chargesAmount")),
            Currency.valueOf(message.getPayload().get("chargesCurrency"))));
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("id", this.id.toString());
    payload.put("merchantId", this.merchantId.toString());
    payload.put("chargesAmount", this.charges.getAmount().toString());
    payload.put("chargesCurrency", this.charges.getCurrency().toString());
    return payload;
  }

  public UUID getId() {
    return id;
  }

  public UUID getMerchantId() {
    return merchantId;
  }

  public Money getCharges() {
    return charges;
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
