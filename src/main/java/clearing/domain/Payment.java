package clearing.domain;

import clearing.domain.event.PaymentClearedEvent;
import clearing.service.DomainEvent;
import java.util.UUID;
import money.Money;
import org.joda.time.DateTime;

public class Payment {

  private UUID id;
  private Scheme scheme;
  private Money value;
  private PaymentStatus status;
  private DateTime paidAt;
  private UUID merchantId;

  public Payment(Scheme scheme, Money value, DateTime paidAt, UUID merchantId) {
    this.scheme = scheme;
    this.value = value;
    this.id = UUID.randomUUID();
    this.merchantId = merchantId;
    this.paidAt = paidAt;
    this.status = PaymentStatus.NEW;
  }

  public void clear(SchemeChargesProvider charges) throws Exception {

    if (!this.status.equals(PaymentStatus.NEW)) {
      throw new RuntimeException("POC001 - Payment is not NEW");
    }

    SchemeCost costs = charges.getForSchemeAt(this.scheme, DateTime.now());

    // Logic goes in here to clear this payment

    this.status = PaymentStatus.CLEARED;

    DomainEvent.publish(
        new PaymentClearedEvent(UUID.randomUUID(), costs.getInterchangeCost(), costs.getSchemeFee(),
            this.value, this.scheme, this.merchantId));
  }

  public UUID getId() {
    return id;
  }

  public UUID getMerchantId() {
    return merchantId;
  }

  public boolean isNew() {
    return this.status.equals(PaymentStatus.NEW);
  }

  public boolean isCleared() {
    return this.status.equals(PaymentStatus.CLEARED);
  }

}
