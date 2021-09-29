package onboarding.domain;

import java.util.UUID;
import onboarding.domain.event.MerchantAssignedToLcpEvent;
import onboarding.service.DomainEvent;

public class Merchant {
  private UUID id;
  private Lcp lcp;

  public Merchant(UUID id) {
    this.id = id;
  }

  public void assignTo(Lcp lcp) throws Exception {
    if (this.lcp != null) {
      throw new RuntimeException(
          "POC011 - Merchant '"
              + this.id.toString()
              + "' is already assigned to LCP '"
              + lcp.toString()
              + "'");
    }
    this.lcp = lcp;
    DomainEvent.publish(new MerchantAssignedToLcpEvent(this.id, this.lcp));
  }

  public UUID getId() {
    return id;
  }

  public boolean isMemberOfLcp(String id) {
    return this.lcp != null && this.lcp.toString().equals(id) ? true : false;
  }
}
