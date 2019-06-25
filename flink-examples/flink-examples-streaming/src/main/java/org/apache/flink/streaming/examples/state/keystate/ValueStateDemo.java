package org.apache.flink.streaming.examples.state.keystate;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * An example of window.
 */
public class ValueStateDemo {
	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.enableCheckpointing(6000);
		env.setStateBackend(new RocksDBStateBackend("file:///Users/bahc/work/data"));

		// set mode to exactly-once (this is the default)
		env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

		// make sure 500 ms of progress happen between checkpoints
		env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);

		// checkpoints have to complete within one minute, or are discarded
		env.getCheckpointConfig().setCheckpointTimeout(60000);

		// allow only one checkpoint to be in progress at the same time
		env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);

		// enable externalized checkpoints which are retained after job cancellation
		env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

		env.addSource(DataSourceAccess.create()).uid("datqsource").keyBy(0).map(new LineFlatProcessor()).uid("flatprocessor").print();

		env.execute("ValueStateDemo");
	}

	private static class DataSourceAccess implements SourceFunction<Tuple2<String, Integer>> {

		private volatile boolean isRunning = true;

		private static DataSourceAccess create() {
			return new DataSourceAccess();
		}

		@Override
		public void run(SourceContext<Tuple2<String, Integer>> ctx) throws Exception {
			while (isRunning) {
				int random = new Random().nextInt(5);
				System.out.println("Random Num. : " + random);
				ctx.collect(new Tuple2<>(String.format("key%s", random), 1));
				Thread.sleep(3000);
			}
		}

		@Override
		public void cancel() {
			isRunning = false;
		}
	}
}
