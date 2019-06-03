package org.apache.flink.streaming.examples.access.ml;

import java.util.ArrayList;
import java.util.List;

public class MlDemo {
	public static void main(String[] args) {
		List<Double> list2 = new ArrayList<>();
		List<String> list1 = new ArrayList<>();
		list1.add("red");
		list1.add("blue");
		list1.add("yellow");

		list1.set(5,"white");

		System.out.println(list1);

		List<Double> red = convertToOneHotVector("red", list1, list2);
		System.out.println(red);

		List<Double> red1 = convertToOneHotVector("blue", list1, list2);
		System.out.println(red1);

		List<Double> red2 = convertToOneHotVector("yellow", list1, list2);
		System.out.println(red2);

	}


	public static List<Double> convertToOneHotVector(Object value, List<String> availables, List<Double> doubleList) {
		if (value == null) {
			return null;
		}
		int len = availables.size();
		List<Double> oneHotValues = new ArrayList<>(len);
		for (int i = 0; i < len; i++) {
			oneHotValues.add(0D);
		}
		if (value instanceof String) {
			int idx = availables.indexOf(value);
			// -1 means non-existence
			if (idx > -1) {
				oneHotValues.set(idx, 1D);
			}
		} else if (value instanceof Number) {
			// Handle value change during different type conversion, eg. Float to Double
			Double d = (Double)value;
			if (doubleList.contains(d)) {
				oneHotValues.set(doubleList.indexOf(d), 1D);
			}
		}
		return oneHotValues;
	}
}
