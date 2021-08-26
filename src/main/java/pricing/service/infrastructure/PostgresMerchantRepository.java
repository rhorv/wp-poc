package pricing.service.infrastructure;

import clearing.domain.Payment;
import clearing.domain.PaymentRepository;
import clearing.domain.PaymentStatus;
import clearing.domain.Scheme;
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
import pricing.domain.BlendedTariff;
import pricing.domain.InactiveMerchant;
import pricing.domain.Merchant;
import pricing.domain.MerchantRepository;
import pricing.domain.PassThroughTariff;

public class PostgresMerchantRepository implements MerchantRepository {

  private Connection connection;
  private String host;
  private Integer port;
  private String user;
  private String pass;
  private String dbName;

  public PostgresMerchantRepository(
      String host, Integer port, String user, String pass, String dbName) {
    this.host = host;
    this.port = port;
    this.user = user;
    this.pass = pass;
    this.dbName = dbName;
  }

  private Connection connect() throws Exception {
    if (this.connection == null) {
      this.connection =
          DriverManager.getConnection(
              "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.dbName,
              this.user,
              this.pass);
    }
    return this.connection;
  }

  private boolean merchantExists(UUID id) throws Exception {
    Connection conn = connect();
    PreparedStatement statement = conn.prepareStatement("SELECT id FROM merchant WHERE id = ?");
    statement.setString(1, id.toString());
    ResultSet resultSet = statement.executeQuery();
    return resultSet.next();
  }

  public Merchant get(UUID id) throws Exception {
    if (!merchantExists(id)) {
      return new InactiveMerchant(id);
    }

    Connection conn = connect();
    PreparedStatement statement = conn.prepareStatement("SELECT * FROM merchant WHERE id = ?");
    statement.setString(1, id.toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();
    if (resultSet.getString("tariff").equals("blended")) {
      return getBlendedMerchant(id);
    } else {
      return getPassThroughMerchant(id);
    }
  }

  private Merchant getBlendedMerchant(UUID id) throws Exception {
    Connection conn = connect();
    PreparedStatement statement =
        conn.prepareStatement("SELECT * FROM blended_merchant WHERE id = ?");
    statement.setString(1, id.toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();
    return new Merchant(
        id,
        new BlendedTariff(
            resultSet.getFloat("charge_percent"),
            new Money(
                resultSet.getInt("fixed_charge_amount"),
                Currency.valueOf(resultSet.getString("fixed_charge_currency")))));
  }

  private Merchant getPassThroughMerchant(UUID id) throws Exception {
    Connection conn = connect();
    PreparedStatement statement =
        conn.prepareStatement("SELECT * FROM passthrough_merchant WHERE id = ?");
    statement.setString(1, id.toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();
    return new Merchant(id, new PassThroughTariff(resultSet.getFloat("charge_percent")));
  }
}
