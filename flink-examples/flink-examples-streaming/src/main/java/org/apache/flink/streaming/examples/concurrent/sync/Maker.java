package org.apache.flink.streaming.examples.concurrent.sync;

public class Maker implements Runnable {

	@Override
	public void run() {
		try {
			for(int i = 0 ;i<10 ;i++){
				Thread.sleep(1000);
				System.out.println("num : " + i);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
