package billing.domain.event;

public abstract class Event {

  public String getCategory() {
    return "event";
  }
}
