package events.formatter.family.rjs1;

import events.IMessage;
import events.formatter.Envelope;
import events.formatter.IProvideSchema;
import events.formatter.ISerializeMessage;
import java.nio.charset.StandardCharsets;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SerializedValidatorDecorator implements ISerializeMessage {

  private ISerializeMessage messageSerializer;
  private IProvideSchema schemaProvider;

  public SerializedValidatorDecorator(
      ISerializeMessage messageSerializer, IProvideSchema schemaProvider) {
    this.messageSerializer = messageSerializer;
    this.schemaProvider = schemaProvider;
  }

  public Envelope serialize(IMessage message) throws Exception {
    Envelope output = this.messageSerializer.serialize(message);

    JSONObject rawSchema = new JSONObject(new JSONTokener(this.schemaProvider.getGenericSchema()));
    Schema schema = SchemaLoader.load(rawSchema);
    schema.validate(
        new JSONObject(
            new String(
                output.getBody(),
                StandardCharsets.UTF_8))); // throws a ValidationException if this object is invalid

    return output;
  }
}
