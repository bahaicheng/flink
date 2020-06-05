package org.apache.flink.streaming.examples.windowing;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class WindowWithWatermark {

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		DataStream<Tuple2<String, Long>> source = env.addSource(new DataSource());
		DataStream<Tuple2<String, Long>> sourceWithWatermark = source.assignTimestampsAndWatermarks(new AssignerWatermarks());
		DataStream<String> apply = sourceWithWatermark.keyBy(1)
			.timeWindow(Time.seconds(5))
			.evictor(TimeEvictor.of(Time.seconds(5)))
			.trigger(EventTimeTrigger.create())
			.apply(new WindowFunction<Tuple2<String, Long>, String, Tuple, TimeWindow>() {
				@Override
				public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple2<String, Long>> input, Collector<String> out) throws Exception {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
					out.collect(String.format("(WindowStart:%s)(WindowEnd:%s)", sdf.format(window.getStart()), sdf.format(window.getEnd())));
				}
			});
		apply.print();
		env.execute();

	}

	private static class SinkAction extends RichSinkFunction {

		@Override
		public void invoke(Object value, Context context) throws Exception {

		}
	}

	private static class AssignerWatermarks implements AssignerWithPeriodicWatermarks<Tuple2<String, Long>> {

		private long currentMaxTimestamp = 0L;

		private long maxOutOfOrderness = 10000L;

		private Watermark watermark = null;

		@Nullable
		@Override
		public Watermark getCurrentWatermark() {
			watermark = new Watermark(currentMaxTimestamp - maxOutOfOrderness);
			return watermark;
		}

		@Override
		public long extractTimestamp(Tuple2<String, Long> element, long previousElementTimestamp) {
			currentMaxTimestamp = Math.max(element.f1, currentMaxTimestamp);
			//System.out.println("WindowWatermarkTest => element : " + element + " currentTime : " + System.currentTimeMillis() + "  currentMaxTimestamp : " + currentMaxTimestamp + "  watermark : " + watermark.getTimestamp());
			return element.f1;
		}
	}

	private static class DataSource implements SourceFunction<Tuple2<String, Long>> {
		private volatile boolean isRunning = true;

		@Override
		public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
			while (isRunning) {
				Thread.sleep(1000);
				ctx.collect(new Tuple2<String, Long>("key" + new Random().nextInt(5), System.currentTimeMillis() - 86400000));
			}
		}

		@Override
		public void cancel() {
			isRunning = false;
		}
	}
}
