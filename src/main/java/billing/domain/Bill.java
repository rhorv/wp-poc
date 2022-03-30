package billing.domain;

import billing.domain.event.BillClosedEvent;
import billing.domain.event.PaymentAddedToBillEvent;
import billing.service.DomainEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import money.Money;

public class Bill {
  private Reference reference;
  private BillStatus status;
  private UUID merchantId;
  private List<Payment> newPayments;
  private Money totalCharges;

  public Bill(UUID merchantId, Reference reference) {
    this.reference = reference;
    this.status = BillStatus.OPEN;
    this.merchantId = merchantId;
    this.newPayments = new ArrayList<>();
  }

  public void close() throws Exception {
    if (!this.status.equals(BillStatus.OPEN)) {
      throw new RuntimeException(
          "POC004 - Bill '"
              + this.reference.toString()
              + "' for merchant '"
              + this.merchantId.toString()
              + "' is not OPEN");
    }
    this.status = BillStatus.CLOSED;
    DomainEvent.publish(new BillClosedEvent(this.merchantId, this.reference, this.totalCharges));
  }

  public void add(Payment payment) throws Exception {
    if (!this.status.equals(BillStatus.OPEN)) {
      throw new RuntimeException(
          "POC004 - Bill '"
              + this.reference.toString()
              + "' for merchant '"
              + this.merchantId.toString()
              + "' is not OPEN");
    }

    this.newPayments.add(payment);
    DomainEvent.publish(
        new PaymentAddedToBillEvent(payment.getId(), this.reference, this.merchantId));
  }

  public boolean hasNewPayment(UUID id) {
    return this.newPayments.stream()
        .filter(payment -> id.equals(payment.getId()))
        .findAny()
        .isPresent();
  }

  public UUID getMerchantId() {
    return merchantId;
  }

  public Reference getReference() {
    return reference;
  }

  public boolean isOpen() {
    return this.status.equals(BillStatus.OPEN);
  }
}
