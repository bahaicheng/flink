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

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

/**
 * ConsumerDataFromKafka.
 */
public class ConsumerDataFromKafka {
	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		Properties prop = new Properties();
		env.setParallelism(1);
		prop.setProperty("bootstrap.servers", "172.20.3.70:9092,172.20.3.71:9092");
		prop.setProperty("group.id", "savepoint_1");
		FlinkKafkaConsumer010<String> kafka010 = new FlinkKafkaConsumer010<String>("savepoint_out_1002", new SimpleStringSchema(), prop);
		DataStream<String> sink = env.addSource(kafka010);
		sink.writeAsText("/Users/bahc/Downloads/savepoint", FileSystem.WriteMode.OVERWRITE);
		env.execute();

	}
}
