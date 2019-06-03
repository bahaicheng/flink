package org.apache.flink.streaming.examples.concurrent.sync;

public class SyncDemo {
	public static void main(String[] args) {
		Car instance1 = new Car();
		instance1.setCountry("American");
		instance1.setName("BMW");
		Thread thread = new Thread(instance1);
		thread.start();
		Car instance2 = new Car();
		instance2.setCountry("Germany");
		instance2.setName("BENZ");
		Thread thread1 = new Thread(instance2);
		thread1.start();

	}


}

class Car implements Runnable{

	private static final Car car = new Car();

	public static Car getInstance() {
		return car;
	}

	private String name;
	private String country;

	public  synchronized void run(){
		synchronized (Car.class) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(String.format("车的品牌：%s 国家：%s", name, country));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
