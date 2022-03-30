package events.formatter.encoder;

import java.util.Base64;

public class Base64Encoder implements IEncodeContent {

  public String getName() {
    return "base64";
  }

  public byte[] encode(byte[] messageContent) throws Exception {
    return Base64.getEncoder().encode(messageContent);
  }
}
