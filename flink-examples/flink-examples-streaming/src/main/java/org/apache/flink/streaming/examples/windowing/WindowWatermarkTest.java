package org.apache.flink.streaming.examples.windowing;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.evictors.TimeEvictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.EventTimeTrigger;

import javax.annotation.Nullable;
import java.util.Random;

public class WindowWatermarkTest {
	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

		env.setParallelism(1);

		DataStream<Tuple3<Integer, Integer, Long>> source = env.addSource(CreateData.creat());

		DataStream<Tuple3<Integer, Integer, Long>> waterMarks = source.assignTimestampsAndWatermarks(GenerateWatermark.create());

		DataStream<Tuple3<Integer, Integer, Long>> sum = waterMarks
			.keyBy(0)
			.window(TumblingEventTimeWindows.of(Time.seconds(3)))
			.evictor(TimeEvictor.of(Time.seconds(5)))
			.trigger(EventTimeTrigger.create())
			.sum(1);

		sum.print();
		env.execute("WaterMark Test");
	}

	private static class GenerateWatermark implements AssignerWithPeriodicWatermarks<Tuple3<Integer, Integer, Long>> {

		private long currentMaxTimestamp = 0L;

		private long maxOutOfOrderness = 2000L;

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
		public long extractTimestamp(Tuple3<Integer, Integer, Long> element, long previousElementTimestamp) {
			currentMaxTimestamp = Math.max(element.f2, currentMaxTimestamp);
			System.out.println("WindowWatermarkTest => element : " + element + " currentTime : " + System.currentTimeMillis() + "  currentMaxTimestamp : " + currentMaxTimestamp + "  watermark : " + watermark);
			return element.f2;
		}
	}


	private static class CreateData implements SourceFunction<Tuple3<Integer, Integer, Long>> {

		private volatile boolean isRunning = true;

		public static CreateData creat() {
			return new CreateData();
		}

		@Override
		public void run(SourceContext<Tuple3<Integer, Integer, Long>> ctx) throws Exception {
			Random r = new Random();
			int i = 0;
			long time = 0;
			while (isRunning) {
				int index = r.nextInt(2);
				long currentTime = System.currentTimeMillis();
				if (i == 0) {
					time = currentTime;
				}
				Tuple3<Integer, Integer, Long> tuple = new Tuple3<Integer, Integer, Long>(index, 1, currentTime);
				Thread.sleep(6000);
				ctx.collect(tuple);
				if (i == 4) {
					Tuple3<Integer, Integer, Long> tuple1 = new Tuple3<Integer, Integer, Long>(index, 1, time);
					ctx.collect(tuple1);
				}
				i++;
			}
		}

		@Override
		public void cancel() {
			isRunning = false;
		}
	}
}
