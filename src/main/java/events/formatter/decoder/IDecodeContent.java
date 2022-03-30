package events.formatter.decoder;

public interface IDecodeContent {
  public String getName();

  public byte[] decode(byte[] messageContent) throws Exception;
}
