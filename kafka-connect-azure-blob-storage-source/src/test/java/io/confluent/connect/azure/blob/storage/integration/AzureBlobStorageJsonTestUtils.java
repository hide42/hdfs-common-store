/*
 * Copyright [2019 - 2019] Confluent Inc.
 */

package io.confluent.connect.azure.blob.storage.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;

/**
 * Utility Class to generate .json file with some dummy data.
 */
public class AzureBlobStorageJsonTestUtils {

  public static Path writeJsonFile(int start, int messages) throws IOException {
    Path file = Files.createTempFile("users-", ".json");
    List<String> lines = new ArrayList<>();
    
    for (int i = start; i < messages; i++) {
      lines.add(new String(createJsonRecordBytes(i)));
    }
    
    Files.write(file, lines);
    return file;
  }

  @SuppressWarnings("resource")
  private static byte[] createJsonRecordBytes(int age) {
    Schema schema = userSchemaConnect();
    JsonConverter jsonConverter = new JsonConverter();
    Map<String, Object> converterConfig = new HashMap<>();
    converterConfig.put("schemas.cache.size", String.valueOf(50));
    jsonConverter.configure(converterConfig, false);
    return jsonConverter.fromConnectData("", schema, userRecordConnect(schema, age));
  }

  public static Schema userSchemaConnect() {
    return org.apache.kafka.connect.data.SchemaBuilder.struct().name("User").version(1)
        .field("firstname", Schema.STRING_SCHEMA)
        .field("lastname", Schema.STRING_SCHEMA)
        .field("age", Schema.INT32_SCHEMA)
        .build();
  }

  public static Struct userRecordConnect(Schema schema, int age) {
    return (new Struct(schema))
        .put("firstname", "Virat")
        .put("lastname", "Kohli")
        .put("age", age);
  }

}
