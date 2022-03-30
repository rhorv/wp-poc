package events.formatter.family.wpcompact.meta;

public interface ConsequenceOfProvider {
  public ConsequenceOf get() throws Exception;

  public void save(ConsequenceOf initiator);
}
