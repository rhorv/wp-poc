package billing.domain;

import java.util.UUID;
import money.Money;

public class Payment {
  private UUID id;
  private Money charge;

  public Payment(UUID id, Money charge) {
    this.id = id;
    this.charge = charge;
  }

  public UUID getId() {
    return id;
  }
}
