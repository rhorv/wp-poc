package events.formatter.decoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class GzipDecoder implements IDecodeContent {

  public String getName() {
    return "gzip";
  }

  public byte[] decode(byte[] messageContent) throws Exception {
    final StringBuilder outStr = new StringBuilder();
    GZIPInputStream content = new GZIPInputStream(new ByteArrayInputStream(messageContent));
    final BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8));
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      outStr.append(line);
    }
    return outStr.toString().getBytes(StandardCharsets.UTF_8);
  }
}
