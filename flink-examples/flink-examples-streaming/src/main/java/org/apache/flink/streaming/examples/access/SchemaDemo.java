package org.apache.flink.streaming.examples.access;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaDemo {


	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger logger = LoggerFactory.getLogger(SchemaDemo.class);

	public static void main(String[] args) throws Exception {

	}

	public static void testCacheModeType(){
		CacheModeType type = null;
		System.out.println(Objects.nonNull(type));
	}

	private static void testJson() throws Exception{
		URL resource = SchemaDemo.class.getClassLoader().getResource("test1.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		Map<String, Object> map = objectMapper.readValue(json, Map.class);
		System.out.println(map);
	}


	public static void testMethod() throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		List<String> init = init();
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema5.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		Map<String, Object> map = objectMapper.readValue(json, Map.class);
		for (String attri: init) {
			Object value = map.get(attri);
			getAtttributes(resultMap, attri, value,true);
		}

		System.out.println("resultMap : "+resultMap);

	}

	private static void getAtttributes(Map<String, Object> rsMap, String attribute, Object value, Boolean combine) {
		if (value instanceof Map) {
			Map<String, Object> node = (Map) value;
			for (Map.Entry<String, Object> entrie : node.entrySet()) {
				if (entrie.getValue() instanceof Map) {
					System.out.println("========");
					getAtttributes(rsMap, entrie.getKey(), entrie.getValue(), false);
				} else {
					rsMap.put(entrie.getKey(), entrie.getValue());
				}
			}
		} else {
			keyBuilder(rsMap, attribute, value, combine);
		}
	}

	private static void keyBuilder(Map<String, Object> rsMap, String attribute, Object value, Boolean combine) {
		String key = attribute.toLowerCase();
		if (combine) {
			String convertedTopic = "topic";
			key = String.format("%s_%s", convertedTopic, key);
		}
		rsMap.put(key, value);
	}

	private static List init(){
		List<String> list = new ArrayList<>();
		list.add("after");
		list.add("op");
		list.add("ts_ms");
		return list;
	}


	private static void typeTest() {
		Map<String, Object> rsMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		List<Object> innerList = new ArrayList<>();
		Map<String, Object> innerMap = new HashMap<>();
		String[] arr = {"6", "7", "8"};
		innerMap.put("key3", "key3");
		innerList.add(innerMap);
		map.put("key1", 1);
		map.put("key2", innerList);
		map.put("key4", arr);
		List<String> list = new ArrayList<>();
		list.add("key1");
		list.add("key2");
		list.add("key4");

		System.out.println(map.get("key1").getClass());
		System.out.println(map.get("key2").getClass());
		System.out.println(map.get("key4").getClass());

	}

	private static void readStringToJson3() throws Exception {
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema5.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		Map<String, Object> map = objectMapper.readValue(json, Map.class);
		System.out.println(map);
		Object aLong = map.get("array");
		getType(aLong);
		Object op = map.get("op");
		if (op == null) {
			System.out.println("op : " + null);
		}
		Object ts_ms = map.get("ts_ms");
		getType(ts_ms);
		Object num = map.get("num");
		getType(num);
		Object dounum = map.get("double");
		getType(dounum);
		Object after = map.get("after");
		System.out.println(after instanceof Map);
		getType(after);
	}

	private static void getType(Object value) {
		System.out.println("value : " + value.getClass());
	}

	private static void readStringToJson2() throws Exception {
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema5.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		JsonNode jsonNode = objectMapper.readTree(json);
		JsonNode after = jsonNode.get("after");
		String s = after.toString();
		System.out.println(s);
		Map<String, Object> map = objectMapper.readValue(s, Map.class);
		Set<Map.Entry<String, Object>> entries = map.entrySet();
		for (Map.Entry<String, Object> entrie : entries) {
			System.out.println(entrie.getKey().getClass() + "   " + entrie.getValue().getClass());
		}
	}

	private static void readStringToJson() throws Exception {
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema5.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		JsonNode jsonNode = objectMapper.readTree(json);
		JsonNode rootid = jsonNode.get("after");
		System.out.println("isContainerNode : " + rootid.isValueNode());
		ObjectNode newID = (ObjectNode) rootid;
		newID.put("fullname", "bahaicheng");
		Map<String, Object> map = new HashMap<>();
		JsonNode id1 = rootid.get("id");
		System.out.println(id1 instanceof JsonNode);
		Iterator<Map.Entry<String, JsonNode>> fields = id1.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> next = fields.next();
			map.put(next.getKey(), next.getValue());
		}
	}


	private static void readStringToJson1() throws Exception {
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema5.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		JsonNode jsonNode = objectMapper.readTree(json);
		JsonNode root = jsonNode.get("after");
		System.out.println("isValueNode : " + root.isValueNode());
		System.out.println("isObject : " + root.isObject());
		System.out.println("isContainerNode : " + root.isContainerNode());
		System.out.println("isArray : " + root.isArray());
		System.out.println("isMissingNode : " + root.isMissingNode());
		System.out.println("===============================");
		JsonNode node = jsonNode.get("num");
		int i = node.asInt();
		System.out.println("node : " + node);
		System.out.println("isValueNode : " + node.isValueNode());
		Map<Object, Object> map = objectMapper.readValue(node.toString(), Map.class);
		System.out.println("map :  " + map);
		System.out.println("isObject : " + node.isObject());
		System.out.println("isContainerNode : " + node.isContainerNode());
		System.out.println("isArray : " + node.isArray());
		System.out.println("isMissingNode : " + node.isMissingNode());

	}


	private static void readJosnFile1() throws Exception {
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema3.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		Map map = objectMapper.readValue(json, Map.class);
		System.out.println(map);
		List<String> fields = (List) map.get("fields");

		String collect = fields.stream().collect(Collectors.joining("\',\'"));
		String reulst = translateIp(collect);
		System.out.println(reulst);

	}

	public static String translateIp(String ips) {
		StringBuilder sbd = new StringBuilder();
		sbd.append("\'");
		sbd.append(ips);
		sbd.append("\'");
		return sbd.toString();
	}

	private static void readJsonFile() throws Exception {
		URL resource = SchemaDemo.class.getClassLoader().getResource("schema.json");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		String json = new String(bytes);
		Map map = objectMapper.readValue(json, Map.class);
		System.out.println(map);
		String jsonStr = objectMapper.writeValueAsString(map);
		System.out.println("jsonStr: " + jsonStr);
		Schema.Parser parser = new Schema.Parser();
		Schema schema = parser.parse(jsonStr);
		System.out.println(schema);
	}
}
