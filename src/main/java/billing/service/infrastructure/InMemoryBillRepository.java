package billing.service.infrastructure;

import billing.domain.Bill;
import billing.domain.BillRepository;
import billing.domain.Reference;
import java.lang.reflect.Field;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class InMemoryBillRepository implements BillRepository {

  private List<Bill> bills = new ArrayList<>();

  private class Key {
    private String merchantId;
    private String reference;

    public Key(String merchantId, String reference) {
      this.merchantId = merchantId;
      this.reference = reference;
    }
  }

  @Override
  public Bill getOpenBillFor(UUID merchantId) throws Exception {
    return this.getOpenBillFor(merchantId, null);
  }

  public Bill getOpenBillFor(UUID merchantId, Reference reference) throws Exception {
    return this.bills.stream()
        .filter(bill -> merchantId.equals(bill.getMerchantId()))
        .filter(bill -> bill.isOpen())
        .filter(bill -> bill.getReference().equals(reference) || reference == null)
        .findFirst().get();
  }

  public void save(Bill bill) throws Exception {
    ListIterator<Bill> iterator = this.bills.listIterator();

    Field merchantField = bill.getClass().getDeclaredField("merchantId");
    merchantField.setAccessible(true);

    Field referenceField = bill.getClass().getDeclaredField("reference");
    referenceField.setAccessible(true);

    UUID billMerchantId = (UUID) merchantField.get(bill);
    UUID billReference = (UUID) referenceField.get(bill);
    while (iterator.hasNext()) {
      Bill storedBill = iterator.next();
      UUID storedMerchantId = (UUID) merchantField.get(storedBill);
      UUID storedReference = (UUID) referenceField.get(storedBill);
      if (storedMerchantId.equals(billMerchantId) && storedReference.equals(billReference)) {
        iterator.remove();
      }
    }
    this.bills.add(bill);
  }


}
