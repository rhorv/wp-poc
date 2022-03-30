package events.formatter.schemaprovider;

import events.formatter.IProvideSchema;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class FileDirectoryTreeBasedSchemaProvider implements IProvideSchema {

  private String basePath;
  private String format;

  public FileDirectoryTreeBasedSchemaProvider(String basePath, String format) {
    this.basePath = basePath;
    this.format = format;
  }

  public String get(String name, String version) throws Exception {
    FileInputStream fis = new FileInputStream(
        this.basePath + File.separatorChar + version + File.separatorChar + name + "." + this.format
    );
    return IOUtils.toString(fis, StandardCharsets.UTF_8);
  }

}
