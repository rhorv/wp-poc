package onboarding.service.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import onboarding.domain.Merchant;
import onboarding.domain.MerchantRepository;

public class InMemoryMerchantRepository implements MerchantRepository {

  private Map<UUID, Merchant> merchants = new HashMap();

  public Merchant get(UUID id) throws Exception {
    if (!this.merchants.containsKey(id)) {
      throw new RuntimeException("POC010 - Merchant '" + id.toString() + "' not found");
    }
    return this.merchants.get(id);
  }

  public void save(Merchant merchant) throws Exception {
    this.merchants.replace(merchant.getId(), merchant);
  }
}
