package events.formatter.schemaprovider;

import events.formatter.IProvideSchema;
import java.io.FileInputStream;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class FileBasedSchemaProvider implements IProvideSchema {

  private String genericSchemaPath;
  private Map<String, String> specificSchemaPath;
  private boolean useGenericForMissing;

  public FileBasedSchemaProvider(String genericSchemaPath,
      Map<String, String> specificSchemaPath, boolean useGenericForMissing) {
    this.genericSchemaPath = genericSchemaPath;
    this.specificSchemaPath = specificSchemaPath;
    this.useGenericForMissing = useGenericForMissing;
  }

  public String getGenericSchema() throws Exception {
    FileInputStream fis = new FileInputStream(this.genericSchemaPath);
    return IOUtils.toString(fis, "UTF-8");
  }

  public String getSpecificSchemaFor(String messageName) throws Exception {
    if (!this.specificSchemaPath.containsKey(messageName)) {
      if (useGenericForMissing) {
        return this.getGenericSchema();
      }
      throw new Exception();
    }
    FileInputStream fis = new FileInputStream(this.specificSchemaPath.get(messageName));
    return IOUtils.toString(fis, "UTF-8");
  }
}
