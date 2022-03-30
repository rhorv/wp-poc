package funding.service.infrastructure;

import funding.domain.FundingBalance;
import funding.domain.FundingBalanceRepository;
import funding.domain.Payment;
import funding.domain.Reference;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import money.Currency;
import money.Money;

public class PostgresFundingBalanceRepository implements FundingBalanceRepository {

  private Connection connection;
  private String host;
  private Integer port;
  private String user;
  private String pass;
  private String dbName;

  public PostgresFundingBalanceRepository(
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

  public FundingBalance getOpenFundingBalanceFor(UUID merchantId) throws Exception {
    Connection conn = connect();
    PreparedStatement statement =
        conn.prepareStatement("SELECT * FROM balance WHERE merchant_id = ? AND status = 'OPEN'");
    statement.setString(1, merchantId.toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();

    if (resultSet == null) {
      throw new RuntimeException(
          "POC009 - There in no open funding balance for merchant '" + merchantId.toString() + "'");
    }
    FundingBalance balance =
        new FundingBalance(merchantId, new Reference(resultSet.getString("reference")));
    Field totalField = balance.getClass().getDeclaredField("totalBalance");
    totalField.setAccessible(true);

    statement =
        conn.prepareStatement(
            "SELECT SUM(value_amount) AS total_balance_value "
                + "FROM payment WHERE merchant_id = ? AND reference = ?");
    statement.setString(1, merchantId.toString());
    statement.setString(2, balance.getReference().toString());
    resultSet = statement.executeQuery();
    resultSet.next();

    // hard wired GBP * POC
    totalField.set(balance, new Money(resultSet.getInt("total_balance_value"), Currency.GBP));
    return balance;
  }

  public void save(FundingBalance balance) throws Exception {
    Connection conn = connect();
    PreparedStatement statement =
        conn.prepareStatement("SELECT * FROM balance WHERE merchant_id = ? AND reference = ?");
    statement.setString(1, balance.getMerchantId().toString());
    statement.setString(2, balance.getReference().toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();

    if (resultSet != null) {
      statement =
          conn.prepareStatement(
              "UPDATE balance SET status = ? WHERE merchant_id = ? AND reference = ?");

      statement.setString(1, balance.isOpen() ? "OPEN" : "CLOSED");
      statement.setString(2, balance.getMerchantId().toString());
      statement.setString(3, balance.getReference().toString());
      statement.execute();
    } else {
      statement =
          conn.prepareStatement("INSERT INTO balance (merchant_id,reference,status) VALUES(?,?,?)");

      statement.setString(1, balance.getMerchantId().toString());
      statement.setString(2, balance.getReference().toString());
      statement.setString(3, balance.isOpen() ? "OPEN" : "CLOSED");
      statement.execute();
    }
    Field paymentsField = balance.getClass().getDeclaredField("newPayments");
    paymentsField.setAccessible(true);
    List<Payment> payments = (ArrayList) paymentsField.get(balance);

    for (Payment payment : payments) {
      statement =
          conn.prepareStatement(
              "INSERT INTO payment (id,merchant_id,reference,value_amount,value_currency) "
                  + "VALUES(?,?,?,?,?)");

      Field chargeField = payment.getClass().getDeclaredField("value");
      chargeField.setAccessible(true);
      Money charge = (Money) chargeField.get(payment);
      statement.setString(1, payment.getId().toString());
      statement.setString(2, balance.getMerchantId().toString());
      statement.setString(3, balance.getReference().toString());
      statement.setInt(4, Integer.valueOf(charge.getAmount().toString()));
      statement.setString(5, charge.getCurrency().toString());
      statement.execute();
    }
  }
}
