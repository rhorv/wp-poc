package events.formatter.family.hav1;

import events.IMessage;
import events.formatter.Envelope;
import events.formatter.ISerializeMessage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

public class Hav1Serializer implements ISerializeMessage {

  private static final String NAME = "hav1";
  private String contentTypeKey;

  public Hav1Serializer(String contentTypeKey) {
    this.contentTypeKey = contentTypeKey;
  }

  public Envelope serialize(IMessage message) throws Exception {

    String id = UUID.randomUUID().toString();
    Parser parser = new Parser();
    Schema avroSchema = parser.parse(BaseSchema.schema);

    GenericRecord avroRecord = new Record(avroSchema);
    avroRecord.put("id", id);
    avroRecord.put("name", message.getName());
    avroRecord.put("category", message.getCategory());
    avroRecord.put("occurredAt", message.getOccurredAt().toString());
    avroRecord.put("version", message.getVersion());
    avroRecord.put("payload", message.getPayload());

    GenericDatumWriter<Object> writer = new GenericDatumWriter<>(avroSchema);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    Encoder encoder = EncoderFactory.get().binaryEncoder(stream, null);
    writer.write(avroRecord, encoder);
    encoder.flush();

    Map<String, String> header = new HashMap<>();
    header.put(this.contentTypeKey, NAME);
    return new Envelope(header, stream.toByteArray());
  }
}