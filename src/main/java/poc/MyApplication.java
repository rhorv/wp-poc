package poc;

public class MyApplication {

  public static void main(String[] args) throws Exception {

    if (args.length == 0) {
      throw new Exception("no app name specified");
    }

    if (args[0].equals("clearing")) {
      System.out.println("Launching clearing application ...");
      clearing.Consumer consumer = new clearing.Consumer();
      consumer.start();
    }
    if (args[0].equals("pricing")) {
      System.out.println("Launching pricing application ...");
      pricing.Consumer consumer = new pricing.Consumer();
      consumer.start();
    }

    throw new Exception("'" + args[0] + "' is not a valid app name");
  }

}
