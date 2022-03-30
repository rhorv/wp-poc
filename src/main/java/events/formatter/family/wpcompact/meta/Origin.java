package events.formatter.family.wpcompact.meta;

import java.util.List;

public class Origin {
  private String contextName;
  private ConsequenceOf consequenceOf;
  private List<MetaItem> generatedBy;

  public Origin(String contextName, ConsequenceOf consequenceOf,
      List<MetaItem> generatedBy) {
    this.contextName = contextName;
    this.consequenceOf = consequenceOf;
    this.generatedBy = generatedBy;
  }

  public String getContextName() {
    return contextName;
  }

  public ConsequenceOf getConsequenceOf() {
    return consequenceOf;
  }

  public List<MetaItem> getGeneratedBy() {
    return generatedBy;
  }
}
