package events.formatter.family.wpcompact.meta.provider;

import events.formatter.family.wpcompact.meta.ConsequenceOf;
import events.formatter.family.wpcompact.meta.ConsequenceOfProvider;

public class InMemoryConsequenceOfProvider implements ConsequenceOfProvider {

  private ConsequenceOf consequenceOf;

  public ConsequenceOf get() throws Exception {
    if (this.consequenceOf == null) {
      throw new Exception();
    }
    return this.consequenceOf;
  }

  public void save(ConsequenceOf item) {
    this.consequenceOf = item;
  }
}
