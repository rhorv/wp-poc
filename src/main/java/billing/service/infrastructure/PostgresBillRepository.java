package billing.service.infrastructure;

import billing.domain.Bill;
import billing.domain.BillRepository;
import billing.domain.Payment;
import billing.domain.Reference;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import money.Currency;
import money.Money;
import org.joda.time.DateTime;

public class PostgresBillRepository implements BillRepository {

  private Connection connection;
  private String host;
  private Integer port;
  private String user;
  private String pass;
  private String dbName;

  public PostgresBillRepository(
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

  public Bill getOpenBillFor(UUID merchantId) throws Exception {
    return this.getOpenBillFor(merchantId, null);
  }

  public Bill getOpenBillFor(UUID merchantId, Reference reference) throws Exception {
    Connection conn = connect();
    PreparedStatement statement =
        (reference == null)
            ? conn.prepareStatement("SELECT * FROM bill WHERE merchant_id = ? AND status = 'OPEN'")
            : conn.prepareStatement(
                "SELECT * FROM bill WHERE merchant_id = ? AND status = 'OPEN' AND reference = ?");
    statement.setString(1, merchantId.toString());
    if (reference != null) {
      statement.setString(2, reference.toString());
    }
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();

    if (resultSet == null) {
      throw new RuntimeException(
          "POC006 - There in no open bill for merchant '" + merchantId.toString() + "'");
    }
    Bill bill = new Bill(merchantId, new Reference(resultSet.getString("reference")));
    Field countField = bill.getClass().getDeclaredField("paymentCount");
    countField.setAccessible(true);

    statement =
        conn.prepareStatement(
            "SELECT COUNT(id) AS payment_count "
                + "FROM payment WHERE merchant_id = ? AND reference = ?");
    statement.setString(1, merchantId.toString());
    statement.setString(2, bill.getReference().toString());
    resultSet = statement.executeQuery();
    resultSet.next();

    countField.set(bill, resultSet.getInt("payment_count"));
    return bill;
  }

  public void save(Bill bill) throws Exception {
    Connection conn = connect();
    PreparedStatement statement =
        conn.prepareStatement("SELECT * FROM bill WHERE merchant_id = ? AND reference = ?");
    statement.setString(1, bill.getMerchantId().toString());
    statement.setString(2, bill.getReference().toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();

    if (resultSet != null) {
      statement =
          conn.prepareStatement(
              "UPDATE bill SET status = ? WHERE merchant_id = ? AND reference = ?");

      statement.setString(1, bill.isOpen() ? "OPEN" : "CLOSED");
      statement.setString(2, bill.getMerchantId().toString());
      statement.setString(3, bill.getReference().toString());
      statement.execute();
    } else {
      statement =
          conn.prepareStatement("INSERT INTO bill (merchant_id,reference,status) VALUES(?,?,?)");

      statement.setString(1, bill.getMerchantId().toString());
      statement.setString(2, bill.getReference().toString());
      statement.setString(3, bill.isOpen() ? "OPEN" : "CLOSED");
      statement.execute();
    }
    Field paymentsField = bill.getClass().getDeclaredField("newPayments");
    paymentsField.setAccessible(true);
    List<Payment> payments = (ArrayList) paymentsField.get(bill);

    for (Payment payment : payments) {
      statement =
          conn.prepareStatement(
              "INSERT INTO payment (id,merchant_id,reference,charge_amount,charge_currency) "
                  + "VALUES(?,?,?,?,?)");

      Field chargeField = payment.getClass().getDeclaredField("charge");
      chargeField.setAccessible(true);
      Money charge = (Money) chargeField.get(payment);
      statement.setString(1, payment.getId().toString());
      statement.setString(2, bill.getMerchantId().toString());
      statement.setString(3, bill.getReference().toString());
      statement.setInt(4, Integer.valueOf(charge.getAmount().toString()));
      statement.setString(5, charge.getCurrency().toString());
      statement.execute();
    }
  }
}
