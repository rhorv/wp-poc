package events.formatter;

import java.util.HashMap;
import java.util.Map;

public class Envelope {

  protected Map<String, String> header = new HashMap<>();
  protected byte[] body;

  public Envelope(Map<String, String> header, byte[] body) throws Exception {
    this.header = header;
    this.body = body;
  }

  public Map<String, String> getHeader() {
    return header;
  }

  public byte[] getBody() {
    return body;
  }

}
