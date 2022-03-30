package events.formatter.family.rjs1;

public class BaseSchema {
  public static final String schema =
      "{\n"
          + "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n"
          + "  \"description\": \"Detailing valid base message format for wp-compact\",\n"
          + "  \"type\": \"object\",\n"
          + "  \"properties\": {\n"
          + "    \"id\": {\n"
          + "      \"description\": \"RFC 4122 Compliant UUID of the message\",\n"
          + "      \"type\": \"string\",\n"
          + "\"pattern\": \"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$\",\n"
          + "      \"examples\": [\n"
          + "        \"622178f0-7dc8-41b6-88a9-2ce0f0934066\"\n"
          + "      ]\n"
          + "    },\n"
          + "    \"name\": {\n"
          + "      \"description\": \"message name\",\n"
          + "      \"type\": \"string\",\n"
          + "      \"pattern\": \"^[a-z_-]+$\",\n"
          + "      \"examples\": [\n"
          + "        \"payment_cleared\"\n"
          + "      ]\n"
          + "    },\n"
          + "    \"category\": {\n"
          + "      \"description\": \"Type of the message\",\n"
          + "      \"type\": \"string\",\n"
          + "      \"enum\": [\n"
          + "        \"event\",\n"
          + "        \"command\"\n"
          + "      ],\n"
          + "      \"examples\": [\n"
          + "        \"event\",\n"
          + "        \"command\"\n"
          + "      ]\n"
          + "    },\n"
          + "    \"payload\": {\n"
          + "      \"description\": \"Payload of the message\",\n"
          + "      \"type\": \"object\"\n"
          + "    },\n"
          + "    \"version\": {\n"
          + "      \"description\": \"Version of the given message (name)\",\n"
          + "      \"type\": \"integer\",\n"
          + "      \"examples\": [\n"
          + "        1\n"
          + "      ]\n"
          + "    },\n"
          + "    \"occurred_at\": {\n"
          + "      \"description\": \"Date and time when the subject"
          + " of the message occurred (ISO 8601)\",\n"
          + "      \"type\": \"string\",\n"
          + "      \"format\": \"date-time\",\n"
          + "      \"examples\": [\n"
          + "        \"2018-09-15T15:53:0001:00\"\n"
          + "      ]\n"
          + "    }\n"
          + "  },\n"
          + "  \"required\": [\n"
          + "    \"category\",\n"
          + "    \"name\",\n"
          + "    \"payload\",\n"
          + "    \"version\",\n"
          + "    \"id\",\n"
          + "    \"occurred_at\"\n"
          + "  ]\n"
          + "}";

}
