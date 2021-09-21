package funding.domain;

import java.util.UUID;
import money.Money;

public class Payment {
  private UUID id;
  private Money value;

  public Payment(UUID id, Money value) {
    this.id = id;
    this.value = value;
  }

  public UUID getId() {
    return id;
  }
}
