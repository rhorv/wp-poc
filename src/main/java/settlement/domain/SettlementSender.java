package settlement.domain;

import java.util.UUID;
import money.Money;

public interface SettlementSender {
  public void send(UUID merchantId, Reference reference, Money charges, Money balance);
}
