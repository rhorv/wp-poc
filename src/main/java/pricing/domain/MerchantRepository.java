package pricing.domain;

import java.util.UUID;

public interface MerchantRepository {
  public Merchant get(UUID id) throws Exception;
}
