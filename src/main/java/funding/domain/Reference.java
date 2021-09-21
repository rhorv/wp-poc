package funding.domain;

public class Reference {
  private String value;

  public Reference(String value) {
    if (!value.matches("^[0-9]{8}$")) {
      throw new RuntimeException(
          "POC007 - '" + value + "' is not a valid funding balance reference");
    }
    this.value = value;
  }

  public String toString() {
    return this.value;
  }
}
