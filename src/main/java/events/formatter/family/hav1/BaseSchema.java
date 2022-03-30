package events.formatter.family.hav1;

public class BaseSchema {
  public static final String schema =
      "{\n"
          + "    \"type\": \"record\",\n"
          + "    \"name\": \"edaAvroGenericSchema\",\n"
          + "    \"namespace\": \"application.avro\",\n"
          + "    \"fields\" : [\n"
          + "      { \"name\": \"name\", \"type\": \"string\", \"default\": \"NONE\" },\n"
          + "      { \"name\": \"id\", \"type\": \"string\", \"logicalType\": \"uuid\" },\n"
          + "      {\n"
          + "        \"name\" : \"payload\",\n"
          + "        \"type\" : { \"type\": \"map\", \"values\": \"string\" }\n"
          + "      },\n"
          + "      { \"name\": \"category\", \"type\": \"string\" },\n"
          + "      { \"name\": \"occurredAt\", \"type\": \"string\" },\n"
          + "      { \"name\": \"version\", \"type\": \"int\" }\n"
          + "    ]\n"
          + "}";
}
