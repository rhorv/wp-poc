package events.formatter;

public interface IProvideSchema {
  public String get(String name, String version) throws Exception;
}
