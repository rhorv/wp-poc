package billing.domain;

import java.util.UUID;

public interface BillRepository {
  public Bill getOpenBillFor(UUID merchantId) throws Exception;

  public Bill getOpenBillFor(UUID merchantId, Reference reference) throws Exception;

  public void save(Bill bill) throws Exception;
}
