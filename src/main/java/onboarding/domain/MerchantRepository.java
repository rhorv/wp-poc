package onboarding.domain;

import java.util.UUID;

public interface MerchantRepository {
  public Merchant get(UUID id) throws Exception;

  public void save(Merchant merchant) throws Exception;
}
