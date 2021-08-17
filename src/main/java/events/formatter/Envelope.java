package events.formatter;

import java.util.HashMap;
import java.util.Map;

public class Envelope {

  public static final String headerVersionKey = "headerVersion";
  private Map<String, String> header = new HashMap<>();
  private byte[] body;

  public Envelope(Map<String, String> header, byte[] body) throws Exception {
    if (!header.containsKey(headerVersionKey)) {
      throw new Exception();
    }
    this.header = header;
    this.body = body;
  }

  public boolean compatibleWith(Integer version) {
    return this.getVersion().equals(version);
  }

  public Integer getVersion() {
    return Integer.valueOf(this.header.get(headerVersionKey));
  }

  public Map<String, String> getHeader() {
    return header;
  }

  public byte[] getBody() {
    return body;
  }

  public static Envelope v1(String messageId, String nameSerde, byte[] body) throws Exception {
    Map<String, String> header = new HashMap<>();
    header.put(Envelope.headerVersionKey, "1");
    header.put("messageId", messageId);
    header.put("nameSerde", nameSerde);
    return new Envelope(header, body);
  }
}
