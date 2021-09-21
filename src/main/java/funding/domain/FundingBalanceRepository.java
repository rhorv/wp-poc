package funding.domain;

import java.util.UUID;

public interface FundingBalanceRepository {
  public FundingBalance getOpenFundingBalanceFor(UUID merchantId) throws Exception;

  public void save(FundingBalance balance) throws Exception;
}
