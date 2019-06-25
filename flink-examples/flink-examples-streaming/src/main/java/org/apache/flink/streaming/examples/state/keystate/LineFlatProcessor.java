package org.apache.flink.streaming.examples.state.keystate;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.Optional;

/**
 * demo.
 */
public class LineFlatProcessor extends RichMapFunction<Tuple2<String, Integer>, Integer> {

	private transient ValueState<Integer> sum;

	@Override
	public void open(Configuration parameters) throws Exception {
		StateTtlConfig stateTtlConfig = StateTtlConfig.newBuilder(Time.milliseconds(6000)).cleanupFullSnapshot().build();

		ValueStateDescriptor<Integer> descriptor = new ValueStateDescriptor("", TypeInformation.of(new TypeHint<Integer>() {
		}));
		descriptor.enableTimeToLive(stateTtlConfig);
		sum = getRuntimeContext().getState(descriptor);
	}

	@Override
	public Integer map(Tuple2<String, Integer> tuple2) throws Exception {
		Integer integer = Optional.ofNullable(sum.value()).orElse(0);
		int i = integer + tuple2.f1;
		sum.update(i);
		return i;
	}
}
