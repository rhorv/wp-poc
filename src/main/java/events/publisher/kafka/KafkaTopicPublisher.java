package events.publisher.kafka;

import events.IMessage;
import events.formatter.Envelope;
import events.formatter.ISerializeMessage;
import events.publisher.IPublish;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaTopicPublisher implements IPublish {

  private ISerializeMessage formatter;
  private String topicName;
  private String server;

  public KafkaTopicPublisher(ISerializeMessage formatter, String server, String topicName) {
    this.formatter = formatter;
    this.topicName = topicName;
    this.server = server;
  }

  private Producer<Long, byte[]> createProducer(String server) {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.LongSerializer");
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.ByteArraySerializer");
    return new KafkaProducer<>(props);
  }

  void runProducer(IMessage message) throws Exception {

    Producer<Long, byte[]> producer = createProducer(this.server);
    long time = System.currentTimeMillis();

    try {
      for (long index = time; index < time + 1; index++) {
        Envelope envelope = this.formatter.serialize(message);
        final ProducerRecord<Long, byte[]> record =
            new ProducerRecord<>(
                this.topicName, index, envelope.getBody());
        for (Map.Entry<String, String> entry : envelope.getHeader().entrySet()) {
          record.headers().add(entry.getKey(), entry.getValue().getBytes(StandardCharsets.UTF_8));
        }

        RecordMetadata metadata = producer.send(record).get();

        long elapsedTime = System.currentTimeMillis() - time;
        System.out.printf(
            "[x] sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
            record.key(), IOUtils.toString(record.value()), metadata.partition(), metadata.offset(),
            elapsedTime);
      }
    } finally {
      producer.flush();
    }
  }

  public void publish(IMessage message) throws Exception {
    runProducer(message);
  }
}
