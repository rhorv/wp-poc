package events.formatter.family.rjs1;

import events.IMessage;
import events.formatter.Envelope;
import events.formatter.ISerializeMessage;
import java.nio.charset.StandardCharsets;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SerializedValidatorDecorator implements ISerializeMessage {

  private ISerializeMessage messageSerializer;
  private String baseSchema;

  public SerializedValidatorDecorator(
      ISerializeMessage messageSerializer, String baseSchema) {
    this.messageSerializer = messageSerializer;
    this.baseSchema = baseSchema;
  }

  public Envelope serialize(IMessage message) throws Exception {
    Envelope output = this.messageSerializer.serialize(message);

    JSONObject rawSchema = new JSONObject(new JSONTokener(this.baseSchema));
    Schema schema = SchemaLoader.load(rawSchema);
    schema.validate(
        new JSONObject(
            new String(
                output.getBody(),
                StandardCharsets.UTF_8))); // throws a ValidationException if this object is invalid

    return output;
  }
}
