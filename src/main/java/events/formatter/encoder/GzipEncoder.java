package events.formatter.encoder;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipEncoder implements IEncodeContent {

  public String getName() {
    return "gzip";
  }

  public byte[] encode(byte[] messageContent) throws Exception {
    ByteArrayOutputStream content = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(content);
    gzip.write(messageContent);
    gzip.flush();
    gzip.close();
    return content.toByteArray();
  }
}
