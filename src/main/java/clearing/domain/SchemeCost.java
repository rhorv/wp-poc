package clearing.domain;

import money.Money;

public class SchemeCost {

  private Scheme scheme;
  private Money interchangeCost;
  private Money schemeFee;

  public SchemeCost(Scheme scheme, Money interchangeCost, Money schemeFee) {
    this.scheme = scheme;
    this.interchangeCost = interchangeCost;
    this.schemeFee = schemeFee;
  }

  public Scheme getScheme() {
    return scheme;
  }

  public Money getInterchangeCost() {
    return interchangeCost;
  }

  public Money getSchemeFee() {
    return schemeFee;
  }
}
