package events.publisher.rabbitmq;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import events.IMessage;
import events.formatter.Envelope;
import events.formatter.ISerializeMessage;
import events.publisher.IPublish;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RabbitMqPublisher implements IPublish {

  private Connection connection;
  private ISerializeMessage formatter;
  private ConnectionFactory connectionFactory;
  private String exchangeName;

  public RabbitMqPublisher(ISerializeMessage formatter, ConnectionFactory connectionFactory,
      String exchangeName) throws Exception {
    this.formatter = formatter;
    this.exchangeName = exchangeName;
    this.connectionFactory = connectionFactory;
  }

  private Connection connect() throws Exception {
    if (this.connection == null) {
      this.connection = this.connectionFactory.newConnection();
    }
    return this.connection;
  }

  public void publish(IMessage message) throws Exception {
    Connection connection = connect();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(this.exchangeName, "fanout", true);
    Envelope envelope = this.formatter.serialize(message);
    Map<String, Object> header = new HashMap<>();
    for (Map.Entry<String, String> entry : envelope.getHeader().entrySet()) {
      header.put(entry.getKey(), entry.getValue());
    }
    BasicProperties properties = new BasicProperties(null, null, header, null, null, null, null,
        null, null, null, null, null, null, null);
    channel.basicPublish(this.exchangeName, "", properties, envelope.getBody());
    System.out
        .println("[x] Published '" + new String(envelope.getBody(), StandardCharsets.UTF_8) + "'");
  }
}
