package events.formatter.family.wpcompact.v1;

import events.IMessage;
import events.Message;
import events.formatter.family.wpcompact.meta.WpMeta;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;

public class WpMessage extends Message {

  private UUID id;
  private WpMeta meta;

  public WpMessage(IMessage message, UUID id, WpMeta meta) {
    this(
        id,
        message.getName(),
        message.getPayload(),
        message.getVersion(),
        message.getOccurredAt(),
        message.getCategory(),
        meta
    );
  }

  public WpMessage(UUID id, String name, Map<String, String> payload, Integer version,
      DateTime occurredAt, String category, WpMeta meta) {
    super(name, payload, version, occurredAt, category);
    this.meta = meta;
    this.id = id;
  }

  public WpMeta getMeta() {
    return meta;
  }

  public UUID getId() {
    return id;
  }
}
