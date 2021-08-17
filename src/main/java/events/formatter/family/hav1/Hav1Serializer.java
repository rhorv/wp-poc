package events.formatter.family.hav1;

import events.IMessage;
import events.formatter.Envelope;
import events.formatter.IProvideSchema;
import events.formatter.ISerializeMessage;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;

public class Hav1Serializer implements ISerializeMessage {

  private IProvideSchema schemaProvider;
  private static final String NAME = "hav1";

  public Hav1Serializer(IProvideSchema schemaProvider) {
    this.schemaProvider = schemaProvider;
  }

  public Envelope serialize(IMessage message) throws Exception {

    String id = UUID.randomUUID().toString();
    Parser parser = new Parser();
    Schema avroSchema = parser.parse(this.schemaProvider.getGenericSchema());

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

    return Envelope.v1(id, NAME, stream.toByteArray());
  }
}