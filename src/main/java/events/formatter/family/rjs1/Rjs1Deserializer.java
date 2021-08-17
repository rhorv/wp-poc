package events.formatter.family.rjs1;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import events.IMessage;
import events.Message;
import events.formatter.Envelope;
import events.formatter.IDeserializeMessage;
import events.formatter.IProvideSchema;
import java.nio.charset.StandardCharsets;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Rjs1Deserializer implements IDeserializeMessage {

  private IProvideSchema schemaProvider;
  public static final String NAME = "rjs1";

  public Rjs1Deserializer(IProvideSchema schemaProvider) {
    this.schemaProvider = schemaProvider;
  }

  public IMessage deserialize(Envelope envelope) throws Exception {

    if (!envelope.compatibleWith(1)) {
      throw new Exception();
    }

    String messageBody = new String(envelope.getBody(), StandardCharsets.UTF_8);
    JSONObject rawSchema = new JSONObject(new JSONTokener(this.schemaProvider.getGenericSchema()));
    Schema schema = SchemaLoader.load(rawSchema);
    schema.validate(
        new JSONObject(messageBody)); // throws a ValidationException if this object is invalid

    GsonBuilder builder = new GsonBuilder();
    Gson gson =
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    GsonClassDto dto = gson.fromJson(messageBody, GsonClassDto.class);

    return new Message(
        dto.name, dto.payload, dto.version, new DateTime(dto.occurredAt), dto.category);
  }
}
