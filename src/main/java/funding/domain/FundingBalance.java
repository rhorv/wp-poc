package funding.domain;

import funding.domain.event.FundingBalanceClosedEvent;
import funding.domain.event.PaymentAddedToFundingBalanceEvent;
import funding.service.DomainEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FundingBalance {
  private Reference reference;
  private FundingBalanceStatus status;
  private UUID merchantId;
  private List<Payment> newPayments;
  private Integer paymentCount = 0;

  public FundingBalance(UUID merchantId, Reference reference) {
    this.reference = reference;
    this.status = FundingBalanceStatus.OPEN;
    this.merchantId = merchantId;
    this.newPayments = new ArrayList<>();
  }

  public void close() throws Exception {
    if (!this.status.equals(FundingBalanceStatus.OPEN)) {
      throw new RuntimeException(
          "POC008 - Funding balance '"
              + this.reference.toString()
              + "' for merchant '"
              + this.merchantId.toString()
              + "' is not OPEN");
    }
    this.status = FundingBalanceStatus.CLOSED;
    DomainEvent.publish(
        new FundingBalanceClosedEvent(this.merchantId, this.reference, this.paymentCount));
  }

  public void add(Payment payment) throws Exception {
    if (!this.status.equals(FundingBalanceStatus.OPEN)) {
      throw new RuntimeException(
          "POC008 - Funding balance '"
              + this.reference.toString()
              + "' for merchant '"
              + this.merchantId.toString()
              + "' is not OPEN");
    }

    this.newPayments.add(payment);
    DomainEvent.publish(
        new PaymentAddedToFundingBalanceEvent(payment.getId(), this.reference, this.merchantId));
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
    return this.status.equals(FundingBalanceStatus.OPEN);
  }
}
