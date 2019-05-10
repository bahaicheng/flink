package org.apache.flink.streaming.examples.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class SchemaDemo {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) throws Exception{
		readJsonFile();
	}

	private static  void readJsonFile() throws Exception{
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema2.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		Map map = objectMapper.readValue(json, Map.class);
		String jsonStr = objectMapper.writeValueAsString(map);
		Schema.Parser parser = new Schema.Parser();
		Schema schema = parser.parse(jsonStr);
		System.out.println(schema);

	}
}
