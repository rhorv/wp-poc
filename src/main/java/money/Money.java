package money;

public class Money {

  private Integer amount;
  private Currency currency;

  public Money(Integer amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public Integer getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public boolean moreThan(Money other) throws Exception {
    if (this.currency != other.getCurrency()) {
      throw new Exception();
    }
    return this.amount > other.getAmount();
  }

  public Money add(Money other) throws Exception {
    if (this.currency != other.getCurrency()) {
      throw new Exception();
    }
    return new Money(this.amount + other.getAmount(), this.currency);
  }

  // this should also take a rounding strategy, but that's not going to be implemented for now
  public Money percentageOf(float percentage) {
    return new Money(Integer.valueOf(Math.round((this.amount / 100) * percentage)), this.currency);
  }

  public boolean equals(Money other) {
    return this.amount.equals(other.getAmount()) && this.currency.equals(other.getCurrency());
  }
}
