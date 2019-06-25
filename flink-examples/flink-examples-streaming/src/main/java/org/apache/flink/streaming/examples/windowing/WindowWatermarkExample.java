package org.apache.flink.streaming.examples.windowing;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * window demo.
 */
public class WindowWatermarkExample {

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
		env.getConfig().setAutoWatermarkInterval(100);

		DataStream<Tuple2<String, Long>> sourceInput = env.addSource(CreateData.creat());

		DataStream<Tuple2<String, Long>> watermark = sourceInput.assignTimestampsAndWatermarks(GenerateWatermark.create());

		DataStream<Tuple2<String, String>> window = watermark
			.keyBy(0)
			.window(SlidingEventTimeWindows.of(Time.seconds(4), Time.seconds(2)))
			.apply(new WindowFunction<Tuple2<String, Long>, Tuple2<String, String>, Tuple, TimeWindow>() {
				@Override
				public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple2<String, Long>> input, Collector<Tuple2<String, String>> out) throws Exception {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					long start = window.getStart();
					long end = window.getEnd();
					long max = window.maxTimestamp();
					String data = input.toString();
					Date sdate = new Date(start);
					String startime = sdf.format(sdate);
					Date edate = new Date(end);
					String endtime = sdf.format(edate);
					out.collect(new Tuple2<>("Data : " + data, "  WindowStart: " + startime + " WindowEnd: " + endtime + " WindowMaxTimestamp: " + max));
				}
			});

		window.print();
		env.execute("WaterMark Test");

	}

	private static class GenerateWatermark implements AssignerWithPeriodicWatermarks<Tuple2<String, Long>> {

		private long currentMaxTimestamp = 0L;

		private long maxOutOfOrderness = 5000L;

		private Watermark watermark = null;

		public static GenerateWatermark create() {
			return new GenerateWatermark();
		}

		@Nullable
		@Override
		public Watermark getCurrentWatermark() {
			watermark = new Watermark(currentMaxTimestamp - maxOutOfOrderness);
			return watermark;
		}

		@Override
		public long extractTimestamp(Tuple2<String, Long> element, long previousElementTimestamp) {
			currentMaxTimestamp = Math.max(element.f1, currentMaxTimestamp);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date date = new Date(element.f1);
			String format = sdf.format(date);
			long l = System.currentTimeMillis();
			String format1 = sdf.format(new Date(l));
			System.out.println("WindowWatermarkTest => Element Time : " + format + " currentTime : " + format1 + "  currentMaxTimestamp : " + currentMaxTimestamp + "  watermark : " + watermark);
			return element.f1;
		}

	}

	private static class CreateData implements SourceFunction<Tuple2<String, Long>> {

		private volatile boolean isRunning = true;

		public static CreateData creat() {
			return new CreateData();
		}

		@Override
		public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
			Random r = new Random();
			int i = 0;
			while (isRunning) {
				SimpleDateFormat sdf = new SimpleDateFormat();
				long time = System.currentTimeMillis() - 86400000;
				String format = sdf.format(new Date(time));
				Tuple2<String, Long> tuple = new Tuple2<String, Long>("000001", time);
				Thread.sleep(1000);
				ctx.collect(tuple);
			}
		}

		@Override
		public void cancel() {
			isRunning = false;
		}
	}
}
