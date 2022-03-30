package events.formatter.family.wpcompact.v1;

import com.google.common.collect.ImmutableList;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import events.IMessage;
import events.formatter.Envelope;
import events.formatter.IDeserializeMessage;
import events.formatter.IProvideSchema;
import events.formatter.decoder.IDecodeContent;
import events.formatter.family.wpcompact.meta.ConsequenceOf;
import events.formatter.family.wpcompact.meta.ConsequenceOfProvider;
import events.formatter.family.wpcompact.meta.InitiatorProvider;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.json.JSONTokener;

public class WpDeserializer implements IDeserializeMessage {

  private Map<String, IDecodeContent> decoders = new HashMap<>();
  private InitiatorProvider initiatorProvider;
  private ConsequenceOfProvider consequenceOfProvider;
  private IProvideSchema schemaProvider;
  private String baseSchema;

  public WpDeserializer(
      List<IDecodeContent> decoders,
      InitiatorProvider initiatorProvider,
      ConsequenceOfProvider consequenceOfProvider,
      IProvideSchema schemaProvider,
      String baseSchema) {
    for (IDecodeContent decoder : decoders) {
      this.decoders.put(decoder.getName(), decoder);
    }
    this.initiatorProvider = initiatorProvider;
    this.consequenceOfProvider = consequenceOfProvider;
    this.schemaProvider = schemaProvider;
    this.baseSchema = baseSchema;
  }

  public IMessage deserialize(Envelope envelope) throws Exception {
    WpEnvelope wpEnvelope = new WpEnvelope(envelope);
    List<String> reversed = ImmutableList.copyOf(wpEnvelope.getContentEncoding()).reverse();

    for (String decoderName : reversed) {
      if (!this.decoders.containsKey(decoderName)) {
        throw new Exception("Could not find a decoder for '" + decoderName + "'");
      }
      wpEnvelope.decodeContent(this.decoders.get(decoderName));
    }
    WpMessage wpMessage = this.format(wpEnvelope);
    this.initiatorProvider.save(wpMessage.getMeta().getInitiator());
    Map<String, String> consequenceOfDetails = new HashMap<>();
    consequenceOfDetails.put("id", wpMessage.getId().toString());
    this.consequenceOfProvider.save(ConsequenceOf.message(consequenceOfDetails));
    return wpMessage;
  }

  private WpMessage format(WpEnvelope envelope) throws Exception {

    String messageBody = new String(envelope.getBody(), StandardCharsets.UTF_8);
    this.validateSchema(messageBody);

    GsonBuilder builder = new GsonBuilder();
    Gson gson =
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    WpMessageGsonClassDto dto = gson.fromJson(messageBody, WpMessageGsonClassDto.class);
    return new WpMessage(
        UUID.fromString(dto.id),
        dto.name,
        dto.payload,
        dto.version,
        DateTime.parse(dto.occurredAt),
        dto.category,
        dto.meta.toMeta());
  }

  private void validateSchema(String body) throws Exception {
    JSONObject rawSchema = new JSONObject(new JSONTokener(this.baseSchema));
    Schema schema = SchemaLoader.load(rawSchema);
    schema.validate(new JSONObject(body));

    JSONObject messageJson = new JSONObject(body);
    JSONObject specificSchema =
        new JSONObject(
            new JSONTokener(
                this.schemaProvider.get(
                    messageJson.get("name").toString(), messageJson.get("version").toString())));
    schema = SchemaLoader.load(specificSchema);
    schema.validate(messageJson);
  }
}
