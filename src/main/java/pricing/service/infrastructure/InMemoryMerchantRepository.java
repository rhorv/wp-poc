package pricing.service.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import pricing.domain.InactiveMerchant;
import pricing.domain.Merchant;
import pricing.domain.MerchantRepository;

public class InMemoryMerchantRepository implements MerchantRepository {

  private Map<UUID, Merchant> merchants = new HashMap<>();

  public Merchant get(UUID id) {
    return this.merchants.containsKey(id) ? this.merchants.get(id) : new InactiveMerchant(id);
  }

  public void save(Merchant merchant) {
    this.merchants.put(merchant.getId(), merchant);
  }

}
