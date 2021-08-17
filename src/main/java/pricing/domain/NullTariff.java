package pricing.domain;

import money.Money;

public class NullTariff implements PricingStrategy {

  @Override
  public Money calculateTotalChargeFor(Payment payment) throws Exception {
    throw new RuntimeException();
  }
}
