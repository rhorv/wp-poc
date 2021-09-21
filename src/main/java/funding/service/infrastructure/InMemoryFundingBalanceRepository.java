package funding.service.infrastructure;

import funding.domain.FundingBalance;
import funding.domain.FundingBalanceRepository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class InMemoryFundingBalanceRepository implements FundingBalanceRepository {

  private List<FundingBalance> balances = new ArrayList<>();

  private class Key {
    private String merchantId;
    private String reference;

    public Key(String merchantId, String reference) {
      this.merchantId = merchantId;
      this.reference = reference;
    }
  }

  public FundingBalance getOpenFundingBalanceFor(UUID merchantId) throws Exception {
    return this.balances.stream()
        .filter(balance -> merchantId.equals(balance.getMerchantId()))
        .filter(balance -> balance.isOpen())
        .findFirst()
        .get();
  }

  public void save(FundingBalance balance) throws Exception {
    ListIterator<FundingBalance> iterator = this.balances.listIterator();

    Field merchantField = balance.getClass().getDeclaredField("merchantId");
    merchantField.setAccessible(true);

    Field referenceField = balance.getClass().getDeclaredField("reference");
    referenceField.setAccessible(true);

    UUID balanceMerchantId = (UUID) merchantField.get(balance);
    UUID balanceReference = (UUID) referenceField.get(balance);
    while (iterator.hasNext()) {
      FundingBalance storedBalance = iterator.next();
      UUID storedMerchantId = (UUID) merchantField.get(storedBalance);
      UUID storedReference = (UUID) referenceField.get(storedBalance);
      if (storedMerchantId.equals(balanceMerchantId) && storedReference.equals(balanceReference)) {
        iterator.remove();
      }
    }
    this.balances.add(balance);
  }
}
