package billing.domain.command;

import billing.domain.Reference;
import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;

public class CloseBillCommand extends Command implements IMessage {

  public static final String NAME = "close_bill";
  private Reference reference;
  private UUID merchantId;
  private DateTime occurredAt;
  private Integer version = 1;

  public CloseBillCommand(Reference reference, UUID merchantId) {
    this.reference = reference;
    this.merchantId = merchantId;
    this.occurredAt = new DateTime();
  }

  public static CloseBillCommand fromMessage(IMessage message) {
    if (!message.getName().matches(CloseBillCommand.NAME)) {
      throw new RuntimeException(
          "POC999 - Incompatible event: '"
              + message.getName()
              + "' cannot be converted to '"
              + NAME
              + "'");
    }
    Map<String, String> payload = message.getPayload();
    return new CloseBillCommand(
        new Reference(message.getPayload().get("reference")),
        UUID.fromString(message.getPayload().get("merchantId")));
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("reference", this.reference.toString());
    payload.put("merchantId", this.merchantId.toString());
    return payload;
  }

  public Reference getReference() {
    return reference;
  }

  public UUID getMerchantId() {
    return merchantId;
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
