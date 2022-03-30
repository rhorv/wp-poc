package events.formatter.family.wpcompact.meta;

public interface InitiatorProvider {
  public Initiator get() throws Exception;

  public void save(Initiator initiator);
}
