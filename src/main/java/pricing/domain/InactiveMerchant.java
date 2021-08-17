package pricing.domain;

import java.util.UUID;
import pricing.domain.event.PaymentChargeCalculationFailedEvent;
import pricing.service.DomainEvent;

public class InactiveMerchant extends Merchant {

  public InactiveMerchant(UUID id) {
    super(id, new NullTariff());
  }

  @Override
  public void calculateCharges(Payment payment) throws Exception {
    DomainEvent
        .publish(new PaymentChargeCalculationFailedEvent(payment.getId(), "Merchant unknown"));
  }
}
