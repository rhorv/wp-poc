package events.formatter.family.rjs1;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import events.IMessage;
import events.formatter.Envelope;
import events.formatter.ISerializeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.kafka.common.protocol.types.Field.Str;

public class Rjs1Serializer implements ISerializeMessage {

  public static final String NAME = "rjs1";
  private String contentTypeKey;

  public Rjs1Serializer(String contentTypeKey) {
    this.contentTypeKey = contentTypeKey;
  }

  public Envelope serialize(IMessage message) throws Exception {
    GsonClassDto dto = new GsonClassDto();
    dto.id = UUID.randomUUID().toString();
    dto.name = message.getName();
    dto.payload = message.getPayload();
    dto.version = message.getVersion();
    dto.occurredAt = message.getOccurredAt().toString();
    dto.category = message.getCategory();

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    String jsonString = gson.toJson(dto);
    Map<String, String> header = new HashMap<>();
    header.put(this.contentTypeKey, NAME);
    return new Envelope(header, jsonString.getBytes());
  }
}
