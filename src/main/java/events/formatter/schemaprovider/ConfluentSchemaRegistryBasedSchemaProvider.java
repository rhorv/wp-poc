package events.formatter.schemaprovider;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import events.formatter.IProvideSchema;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class ConfluentSchemaRegistryBasedSchemaProvider implements IProvideSchema {

  private String host;
  private String genericSchemaName;
  private String family;
  private boolean useGenericForMissing;

  public ConfluentSchemaRegistryBasedSchemaProvider(String host, String genericSchemaName,
      String family, boolean useGenericForMissing) {
    this.host = host;
    this.genericSchemaName = genericSchemaName;
    this.family = family;
    this.useGenericForMissing = useGenericForMissing;
  }

  public String getGenericSchema() throws Exception {
    return this.getSchema(this.genericSchemaName);
  }

  public String getSpecificSchemaFor(String messageName) throws Exception {
    try {
      String response = this.getSchema(messageName);
      return response;
    } catch (Exception e) {
      return this.getGenericSchema();
    }
  }

  private String getSchema(String name) throws Exception {
    URL url = new URL(this.host + "/subjects/" + this.family + "-" + name + "/versions/latest");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    InputStream responseStream = con.getInputStream();
    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser
        .parse(IOUtils.toString(responseStream, StandardCharsets.UTF_8));
    return jsonElement.getAsJsonObject().get("schema").getAsString();
  }
}
