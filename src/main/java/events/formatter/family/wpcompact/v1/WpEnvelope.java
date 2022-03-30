package events.formatter.family.wpcompact.v1;

import events.formatter.Envelope;
import events.formatter.decoder.IDecodeContent;
import events.formatter.encoder.IEncodeContent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WpEnvelope extends Envelope {

  public static final String name = "wp-compact";
  public static final String headerVersionKey = "WP-HeaderVersion";
  public static final String contentEncodingKey = "WP-Content-Encoding";
  public static final String contentTypeKey = "WP-Content-Type";

  private List<String> encoding = new ArrayList<>();

  public WpEnvelope(Envelope envelope) throws Exception {
    super(envelope.getHeader(), envelope.getBody());
    if (!envelope.getHeader().containsKey(headerVersionKey)) {
      throw new Exception("No header version defined");
    }
    if (!envelope.getHeader().containsKey(contentTypeKey)
        || !envelope.getHeader().get(contentTypeKey).equals(name)) {
      throw new Exception("Invalid content type");
    }
    if (envelope.getHeader().containsKey(contentEncodingKey)
        && envelope.getHeader().get(contentEncodingKey).length() > 0) {
      this.encoding.addAll(Arrays.asList(envelope.getHeader().get(contentEncodingKey).split(",")));
    }
  }

  public List<String> getContentEncoding() {
    return this.encoding;
  }

  public void encodeContent(IEncodeContent encoder) throws Exception {
    super.body = encoder.encode(super.body);
    this.encoding.add(encoder.getName());
  }

  public void decodeContent(IDecodeContent decoder) throws Exception {
    super.body = decoder.decode(super.body);
    this.encoding.remove(decoder.getName());
  }

  public Envelope asEnvelope() throws Exception {
    super.header.put(contentEncodingKey, String.join(",", this.encoding));
    super.header.put(contentTypeKey, name);
    super.header.put(headerVersionKey, "1");
    return new Envelope(super.header, super.body);
  }

  public static WpEnvelope fresh(byte[] body) throws Exception {
    Map<String, String> header = new HashMap<>();
    header.put(WpEnvelope.headerVersionKey, "1");
    header.put(WpEnvelope.contentEncodingKey, "");
    header.put(WpEnvelope.contentTypeKey, WpEnvelope.name);
    return new WpEnvelope(new Envelope(header, body));
  }
}
