package events.formatter.encoder;

public interface IEncodeContent {
  public String getName();

  public byte[] encode(byte[] messageContent) throws Exception;
}
