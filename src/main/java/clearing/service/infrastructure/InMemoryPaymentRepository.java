package clearing.service.infrastructure;

import clearing.domain.Payment;
import clearing.domain.PaymentRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryPaymentRepository implements PaymentRepository {

  private Map<UUID, Payment> payments = new HashMap<>();

  public void save(Payment payment) throws Exception {
    this.payments.put(payment.getId(), payment);
  }

  public Payment get(UUID id) throws Exception {
    return this.payments.get(id);
  }
}
