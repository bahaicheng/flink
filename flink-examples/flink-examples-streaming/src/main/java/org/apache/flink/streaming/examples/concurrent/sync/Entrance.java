package org.apache.flink.streaming.examples.concurrent.sync;


public class Entrance {
	public static void main(String[] args) {

	}


	public static void testExecutor(){

	}

	public static void testThead(){
		Maker maker = new Maker();
		Thread thread = new Thread(maker);

		Maker maker1 = new Maker();
		Thread thread1 = new Thread(maker1);
		thread.start();
		thread1.start();
	}
}
