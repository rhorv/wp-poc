package events.formatter.schemaprovider;

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
  private String family;

  public ConfluentSchemaRegistryBasedSchemaProvider(String host, String family) {
    this.host = host;
    this.family = family;
  }

  public String get(String name, String version) throws Exception {
    URL url = new URL(this.host + "/subjects/" + this.family + "-" + name + "/versions/" + version);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    InputStream responseStream = con.getInputStream();
    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser
        .parse(IOUtils.toString(responseStream, StandardCharsets.UTF_8));
    return jsonElement.getAsJsonObject().get("schema").getAsString();
  }
}
