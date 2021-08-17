package pricing.domain;

import money.Money;

public interface PricingStrategy {

  public Money calculateTotalChargeFor(Payment payment) throws Exception;
}
