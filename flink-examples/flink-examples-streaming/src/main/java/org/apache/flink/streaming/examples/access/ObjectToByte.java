package org.apache.flink.streaming.examples.access;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ObjectToByte {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) throws Exception{
		readJosnFile1();

	}

	private static void readJosnFile1() throws Exception {
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema4.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		JsonNode jsonNode = objectMapper.readTree(json);
		JsonNode fields = jsonNode.findValue("fields");
		ObjectNode node2 = (ObjectNode) fields;
		node2.put("ip", "8888888");
		ObjectNode node3 = (ObjectNode) jsonNode;
		node3.put("aaaaa","bbbbbbb");
		byte[] payload = objectMapper.writeValueAsBytes(jsonNode);
		System.out.println(objectMapper.writeValueAsString(jsonNode));

	}

	public static byte[] toByteArray(Object obj, RtEvent in) {
		byte[] obj1 = (byte[]) obj;
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

}
