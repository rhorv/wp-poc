package pricing.domain;


import java.util.UUID;
import money.Money;
import pricing.domain.event.PaymentChargeCalculatedEvent;
import pricing.service.DomainEvent;

public class Merchant {

  protected UUID id;
  protected PricingStrategy pricingStrategy;

  public Merchant(UUID id, PricingStrategy pricingStrategy) {
    this.id = id;
    this.pricingStrategy = pricingStrategy;
  }

  public void calculateCharges(Payment payment) throws Exception {
    Money charge = this.pricingStrategy.calculateTotalChargeFor(payment);
    DomainEvent.publish(new PaymentChargeCalculatedEvent(payment.getId(), charge, this.id));
  }

  public UUID getId() {
    return id;
  }
}
