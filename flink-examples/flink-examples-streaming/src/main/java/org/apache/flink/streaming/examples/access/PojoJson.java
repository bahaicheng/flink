package org.apache.flink.streaming.examples.access;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class PojoJson {
	public static void main(String[] args) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		Person person = new Person();
		Hehavior hehavior = new Hehavior();
		hehavior.setSmile("YES");
		hehavior.setCry(true);
		person.setAge(18);
		person.setName("bahc");
		person.setHehavior(hehavior);
		String s = objectMapper.writeValueAsString(person);
		System.out.println(s);
		Map map = objectMapper.readValue(s, Map.class);
		System.out.println(map.get("name").getClass());
		System.out.println(map.get("age").getClass());
		System.out.println(map.get("hehavior").getClass());

	}
}

class Person {
	private String name;

	private int age;

	private Hehavior hehavior;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Hehavior getHehavior() {
		return hehavior;
	}

	public void setHehavior(Hehavior hehavior) {
		this.hehavior = hehavior;
	}
}

class Hehavior {
	private String smile;

	private boolean cry;

	public String getSmile() {
		return smile;
	}

	public void setSmile(String smile) {
		this.smile = smile;
	}

	public boolean isCry() {
		return cry;
	}

	public void setCry(boolean cry) {
		this.cry = cry;
	}
}
