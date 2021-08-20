package clearing.service.infrastructure;

import clearing.domain.Payment;
import clearing.domain.PaymentRepository;
import clearing.domain.PaymentStatus;
import clearing.domain.Scheme;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;
import sun.reflect.ReflectionFactory;

public class PostgresPaymentRepository implements PaymentRepository {

  private Connection connection;
  private String host;
  private Integer port;
  private String user;
  private String pass;
  private String dbName;

  public PostgresPaymentRepository(String host, Integer port, String user, String pass,
      String dbName) {
    this.host = host;
    this.port = port;
    this.user = user;
    this.pass = pass;
    this.dbName = dbName;
  }

  private Connection connect() throws Exception {
    if (this.connection == null) {
      this.connection = DriverManager.getConnection(
          "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.dbName, this.user,
          this.pass);
    }
    return this.connection;
  }

  public void save(Payment payment) throws Exception {
    Connection conn = connect();

    if (paymentExists(payment.getId())) {
      PreparedStatement statement = conn.prepareStatement(
          "UPDATE payment SET merchant_id = ?, total_value = ?, "
              + "currency = ?, scheme = ?, paid_at = ?, status = ?");

      Field merchantField = payment.getClass().getDeclaredField("merchant_id");
      merchantField.setAccessible(true);
      statement.setString(1, ((UUID) merchantField.get(payment)).toString());

      Field valueField = payment.getClass().getDeclaredField("value");
      valueField.setAccessible(true);
      statement
          .setInt(2, Integer.valueOf(((Money) valueField.get(payment)).getAmount().toString()));
      statement.setString(3, ((Money) valueField.get(payment)).getCurrency().toString());

      Field schemeField = payment.getClass().getDeclaredField("scheme");
      schemeField.setAccessible(true);
      statement.setString(4, ((Scheme) schemeField.get(payment)).toString());

      Field paidAtField = payment.getClass().getDeclaredField("paid_at");
      paidAtField.setAccessible(true);
      statement.setTimestamp(5, new Timestamp(((DateTime) paidAtField.get(payment)).getMillis()));

      Field statusField = payment.getClass().getDeclaredField("status");
      statusField.setAccessible(true);
      statement.setString(6, ((PaymentStatus) statusField.get(payment)).toString());

      statement.executeQuery();
    } else {
      PreparedStatement statement = conn.prepareStatement(
          "INSERT INTO payment (id, merchant_id, total_value, currency, "
              + "scheme, paid_at, status) VALUES (?,?,?,?,?,?,?)");

      Field idField = payment.getClass().getDeclaredField("id");
      idField.setAccessible(true);
      statement.setString(1, ((UUID) idField.get(payment)).toString());

      Field merchantField = payment.getClass().getDeclaredField("merchant_id");
      merchantField.setAccessible(true);
      statement.setString(2, ((UUID) merchantField.get(payment)).toString());

      Field valueField = payment.getClass().getDeclaredField("value");
      valueField.setAccessible(true);
      statement
          .setInt(3, Integer.valueOf(((Money) valueField.get(payment)).getAmount().toString()));
      statement.setString(4, ((Money) valueField.get(payment)).getCurrency().toString());

      Field schemeField = payment.getClass().getDeclaredField("scheme");
      schemeField.setAccessible(true);
      statement.setString(5, ((Scheme) schemeField.get(payment)).toString());

      Field paidAtField = payment.getClass().getDeclaredField("paid_at");
      paidAtField.setAccessible(true);
      statement.setTimestamp(6, new Timestamp(((DateTime) paidAtField.get(payment)).getMillis()));

      Field statusField = payment.getClass().getDeclaredField("status");
      statusField.setAccessible(true);
      statement.setString(7, ((PaymentStatus) statusField.get(payment)).toString());

      statement.executeQuery();
    }
  }

  private boolean paymentExists(UUID id) throws Exception {
    Connection conn = connect();
    PreparedStatement statement = conn.prepareStatement("SELECT id FROM payment WHERE id = ?");
    statement.setString(1, id.toString());
    ResultSet resultSet = statement.executeQuery();
    return resultSet.getFetchSize() > 0;
  }

  public Payment get(UUID id) throws Exception {
    Connection conn = connect();
    PreparedStatement statement = conn.prepareStatement("SELECT * FROM payment WHERE id = ?");
    statement.setString(1, id.toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();
    Class def = Class.forName("clearing.domain.Payment");
    ReflectionFactory rf =
        ReflectionFactory.getReflectionFactory();
    Constructor objDef = def.getDeclaredConstructor();
    Constructor intConstr = rf.newConstructorForSerialization(
        def, objDef
    );
    Payment payment = (Payment) def.cast(intConstr.newInstance());

    Field statusField = payment.getClass().getDeclaredField("status");
    statusField.setAccessible(true);
    statusField.set(payment, PaymentStatus.valueOf(resultSet.getString("status")));

    Field idField = payment.getClass().getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(payment, UUID.fromString(resultSet.getString("id")));

    Field valueField = payment.getClass().getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(payment, new Money(resultSet.getInt("total_value"),
        Currency.valueOf(resultSet.getString("currency"))));

    Field merchantField = payment.getClass().getDeclaredField("merchant_id");
    merchantField.setAccessible(true);
    merchantField.set(payment, UUID.fromString(resultSet.getString("merchant_id")));

    Field paidAtField = payment.getClass().getDeclaredField("paid_at");
    paidAtField.setAccessible(true);
    paidAtField.set(payment, new DateTime(resultSet.getTimestamp("timestamp")));

    Field schemeField = payment.getClass().getDeclaredField("scheme");
    schemeField.setAccessible(true);
    schemeField.set(payment, Scheme.valueOf(resultSet.getString("scheme")));

    return payment;
  }
}
