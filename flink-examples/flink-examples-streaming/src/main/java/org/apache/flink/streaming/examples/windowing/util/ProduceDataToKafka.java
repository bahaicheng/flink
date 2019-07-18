/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.streaming.examples.windowing.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;


/**
 * ProduceDataToKafka.
 */
public class ProduceDataToKafka {

	private static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<String> source = env.addSource(JosnSource.create());
		FlinkKafkaProducer010<String> sink = new FlinkKafkaProducer010<String>("172.20.3.70:9092,172.20.3.71:9092", "savepoint_in_1002", new SimpleStringSchema());
		source.addSink(sink).setParallelism(1);
		env.execute();

	}

	private static class JosnSource implements SourceFunction<String> {
		public static JosnSource create() {
			return new JosnSource();
		}
		@Override
		public void run(SourceContext<String> ctx) throws Exception {
			int i = 0;
			while (true) {
				Thread.sleep(5000);
				++i;
				UserInfo user = new UserInfo("name-" + i, i % 2 == 0 ? "m" : "f", i, "tel-" + i, "address-" + i);
				String out = mapper.writeValueAsString(user);
				System.out.println(out);
				ctx.collect(out);
			}
		}
		@Override
		public void cancel() {

		}
	}
}
