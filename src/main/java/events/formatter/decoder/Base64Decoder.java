package events.formatter.decoder;

import java.util.Base64;

public class Base64Decoder implements IDecodeContent {

  public String getName() {
    return "base64";
  }

  public byte[] decode(byte[] messageContent) throws Exception {
    return Base64.getDecoder().decode(messageContent);
  }

}
