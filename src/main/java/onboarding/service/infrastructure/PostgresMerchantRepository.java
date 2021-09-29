package onboarding.service.infrastructure;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import onboarding.domain.Lcp;
import onboarding.domain.Merchant;
import onboarding.domain.MerchantRepository;

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
      throw new RuntimeException("POC010 - Merchant '" + id.toString() + "' does not exist");
    }

    Connection conn = connect();
    PreparedStatement statement = conn.prepareStatement("SELECT * FROM merchant WHERE id = ?");
    statement.setString(1, id.toString());
    ResultSet resultSet = statement.executeQuery();
    resultSet.next();

    Merchant merchant = new Merchant(id);
    String lcpId = resultSet.getString("lcp_id");
    if (!resultSet.wasNull()) {
      Lcp lcp = new Lcp(lcpId);

      Field lcpField = merchant.getClass().getDeclaredField("lcp");
      lcpField.setAccessible(true);

      lcpField.set(merchant, lcp);
    }
    return merchant;
  }

  public void save(Merchant merchant) throws Exception {
    Connection conn = connect();
    Field lcpField = merchant.getClass().getDeclaredField("lcp");
    lcpField.setAccessible(true);
    PreparedStatement statement =
        conn.prepareStatement("UPDATE merchant SET lcp_id = ? WHERE id = ?");
    statement.setString(1, merchant.getId().toString());
    statement.setString(2, lcpField.get(merchant).toString());
    statement.execute();
  }
}
