package events.formatter.family.wpcompact.meta.provider;

import events.formatter.family.wpcompact.meta.Initiator;
import events.formatter.family.wpcompact.meta.InitiatorProvider;

public class InMemoryInitiatorProvider implements InitiatorProvider {

  private Initiator initiator;

  public Initiator get() throws Exception {
    if (this.initiator == null) {
      throw new Exception();
    }
    return this.initiator;
  }

  public void save(Initiator item) {
    this.initiator = item;
  }
}
