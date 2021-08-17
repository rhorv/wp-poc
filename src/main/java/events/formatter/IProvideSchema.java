package events.formatter;

public interface IProvideSchema {

  public String getGenericSchema() throws Exception;

  public String getSpecificSchemaFor(String messageName) throws Exception;
}
