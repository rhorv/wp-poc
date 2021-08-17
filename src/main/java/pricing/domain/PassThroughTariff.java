package pricing.domain;

import money.Money;

public class PassThroughTariff implements PricingStrategy {

  private float merchantChargePercent;

  public PassThroughTariff(float merchantChargePercent) {
    this.merchantChargePercent = merchantChargePercent;
  }

  public Money calculateTotalChargeFor(Payment payment) throws Exception {
    return payment.getValue().percentageOf(this.merchantChargePercent)
        .add(payment.getInterChangeFee())
        .add(payment.getSchemeFee());
  }
}
