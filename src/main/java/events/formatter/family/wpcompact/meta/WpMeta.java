package events.formatter.family.wpcompact.meta;

public class WpMeta {
  private Origin origin;
  private Initiator initiator;

  public WpMeta(Origin origin, Initiator initiator) {
    this.origin = origin;
    this.initiator = initiator;
  }

  public Origin getOrigin() {
    return origin;
  }

  public Initiator getInitiator() {
    return initiator;
  }
}
