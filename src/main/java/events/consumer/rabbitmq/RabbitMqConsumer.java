package events.consumer.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import events.consumer.IConsume;
import events.dispatcher.IDispatch;
import events.formatter.IDeserializeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RabbitMqConsumer implements IConsume {

  private Connection connection;
  private IDeserializeMessage formatter;
  private IDispatch dispatcher;
  private ConnectionFactory connectionFactory;
  private String queueName;

  public RabbitMqConsumer(
      IDeserializeMessage formatter,
      IDispatch dispatcher,
      ConnectionFactory connectionFactory,
      String queueName
  ) throws Exception {
    this.formatter = formatter;
    this.dispatcher = dispatcher;
    this.connectionFactory = connectionFactory;
    this.queueName = queueName;
  }

  private Connection connect() throws Exception {
    if (this.connection == null) {
      this.connection = this.connectionFactory.newConnection();
    }
    return this.connection;
  }

  public void consume() throws Exception {

    Connection connection = connect();
    Channel channel = connection.createChannel();
    channel.basicConsume(this.queueName, false, "myConsumerTag",
        new DefaultConsumer(channel) {
          public void handleDelivery(String consumerTag,
              Envelope envelope,
              AMQP.BasicProperties properties,
              byte[] body)
              throws IOException {
            String routingKey = envelope.getRoutingKey();
            String contentType = properties.getContentType();
            long deliveryTag = envelope.getDeliveryTag();
            System.out.println("[x] Received '" + new String(body, StandardCharsets.UTF_8) + "'");
            Map<String, String> header = new HashMap<>();
            for (Map.Entry<String, Object> entry : properties.getHeaders().entrySet()) {
              header.put(entry.getKey(), entry.getValue().toString());
            }
            try {
              dispatcher
                  .dispatch(formatter.deserialize(new events.formatter.Envelope(header, body)));
              channel.basicAck(deliveryTag, false);
            } catch (Exception e) {
              channel.basicNack(deliveryTag, false, true);
              e.printStackTrace();
            }
          }
        });
  }
}
