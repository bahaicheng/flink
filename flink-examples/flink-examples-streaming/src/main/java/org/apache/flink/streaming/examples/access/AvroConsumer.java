package org.apache.flink.streaming.examples.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class AvroConsumer {

	private static final String SCHEMA = "{\"type\": \"record\", \"name\": \"User\", \"fields\": [{\"name\": \"id\", \"type\": \"int\"}, {\"name\": \"name\", \"type\": \"string\"}, {\"name\": \"age\", \"type\": \"int\"}]}";


	public static final String USER_SCHEMA = "{\"type\": \"record\", \"name\": \"User\", " +
		"\"fields\": [{\"name\": \"id\", \"type\": \"int\"}, " +
		"{\"name\": \"name\",  \"type\": \"string\"}]}";

    public static void main(String[] args) throws Exception{
		Logger logger = LoggerFactory.getLogger(AvroConsumer.class);
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "test");
        FlinkKafkaConsumer010<RtEvent> consumer11 = new FlinkKafkaConsumer010<>("dev3-yangyunhe-topic001", new KafkaMessageDeserializationSchema() , properties);

        DataStream<RtEvent> source = env.addSource(consumer11);

		DataStream<String> map = source.map(new MapFunction<RtEvent, String>() {
			@Override
			public String map(RtEvent value) throws Exception {
				ObjectMapper objectMapper = new ObjectMapper();
				byte[] payload = value.getPayload();
				KafkaAvroDeserializer deserializer = new KafkaAvroDeserializer();
				Schema.Parser parser = new Schema.Parser();
				Map map = objectMapper.readValue(SCHEMA, Map.class);
				Schema schema = parser.parse(objectMapper.writeValueAsString(map));
				deserializer.configure(Collections.singletonMap("schema.registry.url","http://localhost:8081"),false);
				GenericRecord deserialize =(GenericRecord) deserializer.deserialize("dev3-yangyunhe-topic001", payload,schema);
				logger.info("----------------------------------");
				return deserialize.toString();
			}
		});
		map.print();
		env.execute();

    }
}
