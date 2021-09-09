package billing.domain;

public class Reference {
  private String value;

  public Reference(String value) {
    if (!value.matches("^[0-9]{8}$")) {
      throw new RuntimeException("POC003 - '" + value + "' is not a valid bill reference");
    }
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
