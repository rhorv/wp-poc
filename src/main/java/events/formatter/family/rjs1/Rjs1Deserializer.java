package events.formatter.family.rjs1;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import events.IMessage;
import events.Message;
import events.formatter.Envelope;
import events.formatter.IDeserializeMessage;
import java.nio.charset.StandardCharsets;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Rjs1Deserializer implements IDeserializeMessage {

  public static final String NAME = "rjs1";
  private String baseSchema;

  public Rjs1Deserializer(String baseSchema) {
    this.baseSchema = baseSchema;
  }

  public IMessage deserialize(Envelope envelope) throws Exception {

    String messageBody = new String(envelope.getBody(), StandardCharsets.UTF_8);
    JSONObject rawSchema = new JSONObject(new JSONTokener(this.baseSchema));
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
