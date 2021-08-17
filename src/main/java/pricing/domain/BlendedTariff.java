package pricing.domain;

import money.Money;

public class BlendedTariff implements PricingStrategy {

  private float merchantChargePercent;
  private Money fixedCharge;

  public BlendedTariff(float merchantChargePercent, Money fixedCharge) {
    this.merchantChargePercent = merchantChargePercent;
    this.fixedCharge = fixedCharge;
  }

  public Money calculateTotalChargeFor(Payment payment) throws Exception {
    return payment.getValue().percentageOf(this.merchantChargePercent).add(this.fixedCharge);
  }
}
