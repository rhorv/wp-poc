package clearing.domain;

import java.util.UUID;

public interface PaymentRepository {

  public void save(Payment payment) throws Exception;

  public Payment get(UUID id) throws Exception;
}
