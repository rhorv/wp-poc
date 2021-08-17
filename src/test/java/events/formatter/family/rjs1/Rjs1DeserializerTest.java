package events.formatter.family.rjs1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import events.IMessage;
import events.formatter.Envelope;
import events.formatter.IProvideSchema;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Rjs1DeserializerTest {

  private String testSchema = "{\n"
      + "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n"
      + "  \"description\": \"Detailing valid message format\",\n"
      + "  \"type\": \"object\",\n"
      + "  \"properties\": {\n"
      + "    \"id\": {\n"
      + "      \"description\": \"RFC 4122 Compliant UUID of the message\",\n"
      + "      \"type\": \"string\",\n"
      + "      \"pattern\": \"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$\",\n"
      + "      \"examples\": [\n"
      + "        \"622178f0-7dc8-41b6-88a9-2ce0f0934066\"\n"
      + "      ]\n"
      + "    },\n"
      + "    \"name\": {\n"
      + "      \"description\": \"message name\",\n"
      + "      \"type\": \"string\",\n"
      + "      \"pattern\": \"^[a-z_-]+$\",\n"
      + "      \"examples\": [\n"
      + "        \"transaction_cleared\"\n"
      + "      ]\n"
      + "    },\n"
      + "    \"category\": {\n"
      + "      \"description\": \"Type of the message\",\n"
      + "      \"type\": \"string\",\n"
      + "      \"enum\": [\"event\", \"command\"],\n"
      + "      \"examples\": [\n"
      + "        \"event\",\n"
      + "        \"command\"\n"
      + "      ]\n"
      + "    },\n"
      + "    \"payload\": {\n"
      + "      \"description\": \"Payload of the message\",\n"
      + "      \"type\": \"object\"\n"
      + "    },\n"
      + "    \"version\": {\n"
      + "      \"description\": \"Version of the given message (name)\",\n"
      + "      \"type\": \"integer\",\n"
      + "      \"examples\": [\n"
      + "        1\n"
      + "      ]\n"
      + "    },\n"
      + "    \"occurred_at\": {\n"
      + "      \"description\": \"Date and time when the subject of the message occurred (ISO 8601)\",\n"
      + "      \"type\": \"string\",\n"
      + "      \"format\": \"date-time\",\n"
      + "      \"examples\": [\n"
      + "        \"2018-09-15T15:53:0001:00\"\n"
      + "      ]\n"
      + "    }\n"
      + "  },\n"
      + "  \"required\": [ \"category\", \"name\", \"payload\", \"version\", \"id\", \"occurred_at\" ]\n"
      + "}";

  private Rjs1Deserializer deserializer;
  private IProvideSchema provider;

  @BeforeEach
  public void setUp() throws Exception {
    this.provider = mock(IProvideSchema.class);
    this.deserializer = new Rjs1Deserializer(this.provider);
  }

  @Test
  void testItThrowsOnNotValidJsonString() throws Exception {
    String invalidJson = "invalid";
    when(this.provider.getGenericSchema()).thenReturn("{}");
    assertThrows(
        Exception.class,
        () -> {
          this.deserializer.deserialize(
              Envelope.v1(UUID.randomUUID().toString(), Rjs1Deserializer.NAME,
                  invalidJson.getBytes(StandardCharsets.UTF_8)));
        });
  }

  @Test
  void testItThrowsOnNonSchemaCompliantJson() throws Exception {
    String nonCompliantJson = "{}";
    when(this.provider.getGenericSchema())
        .thenReturn(
            "{\n"
                + "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n"
                + "  \"type\": \"object\",\n"
                + "  \"properties\": {\n"
                + "    \"somefield\": {\n"
                + "      \"type\": \"string\"\n"
                + "    }\n"
                + "  },\n"
                + "  \"required\": [ \"somefield\" ]\n"
                + "}");
    assertThrows(
        Exception.class,
        () -> {
          this.deserializer.deserialize(
              Envelope.v1(UUID.randomUUID().toString(), Rjs1Deserializer.NAME,
                  nonCompliantJson.getBytes(StandardCharsets.UTF_8)));
        });
  }

  @Test
  void testItThrowsOnNonCompatibleEnvelope() throws Exception {
    String json = "{}";
    when(this.provider.getGenericSchema()).thenReturn("{}");

    Map<String, String> header = new HashMap<String, String>();
    header.put("headerVersion", "0");

    assertThrows(
        Exception.class,
        () -> {
          this.deserializer
              .deserialize(new Envelope(header, json.getBytes(StandardCharsets.UTF_8)));
        });
  }

  @Test
  void testItDeserializesCorrectlyIntoAMessageForValidJson() throws Exception {
    String validJson =
        "{\"id\": \"622178f0-7dc8-41b6-88a9-2ce0f0934066\",\"name\": \"event_name\",\"category\": \"event\",\"payload\": {\"field\": \"value\"},\"occurred_at\": \"2020-09-15T15:53:00+01:00\",\"version\": 1}";
    when(this.provider.getGenericSchema()).thenReturn(this.testSchema);

    IMessage message =
        this.deserializer.deserialize(
            Envelope.v1(UUID.randomUUID().toString(), Rjs1Deserializer.NAME,
                validJson.getBytes(StandardCharsets.UTF_8)));
    assertEquals(message.getName(), "event_name");
    assertEquals(message.getCategory(), "event");
    assertEquals(message.getVersion(), 1);
    assertEquals(message.getOccurredAt(), new DateTime("2020-09-15T15:53:00+01:00"));
    assertEquals(message.getPayload().get("field"), "value");
    assertEquals(message.getPayload().size(), 1);
  }
}
