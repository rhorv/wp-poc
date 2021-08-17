package clearing.domain.command;

import events.IMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;

public class ClearPaymentCommand extends Command implements IMessage {

  public static final String NAME = "clear_payment";
  private UUID paymentId;
  private DateTime occurredAt;
  private Integer version = 1;

  public ClearPaymentCommand(UUID paymentId) {
    this.paymentId = paymentId;
    this.occurredAt = DateTime.now();
  }

  public static ClearPaymentCommand fromMessage(IMessage message) {
    if (!message.getName().matches(ClearPaymentCommand.NAME)) {
      throw new RuntimeException();
    }
    Map<String, String> payload = message.getPayload();
    return new ClearPaymentCommand(
        UUID.fromString(payload.get("id"))
    );
  }

  public Map<String, String> getPayload() {
    Map<String, String> payload = new HashMap<String, String>();
    payload.put("id", this.paymentId.toString());
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
