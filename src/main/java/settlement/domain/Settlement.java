package settlement.domain;

import java.util.UUID;
import money.Money;

public class Settlement {
  private UUID merchantId;
  private Reference reference;
  private Money totalCharge;
  private Money totalBalance;
  private SettlementStatus status;

  public Settlement(UUID merchantId, Reference reference) {
    this.merchantId = merchantId;
    this.reference = reference;
    this.status = SettlementStatus.OPEN;
  }

  private void addCharges(Money charge) {
    if (this.totalCharge != null) {
      throw new RuntimeException("POC012 - This settlement already has charges");
    }
    this.totalCharge = charge;
    if (this.totalBalance != null) {
      this.status = SettlementStatus.CLOSED;
    }
  }

  private void addBalance(Money balance) {
    if (this.totalBalance != null) {
      throw new RuntimeException("POC012 - This settlement already has a balance");
    }
    this.totalBalance = balance;
    if (this.totalCharge != null) {
      this.status = SettlementStatus.CLOSED;
    }
  }

  private void send() {

  }
}
