package TSPSolver.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import TSPSolver.Exception.TSPException;

public class Path implements Comparable<Path> {

	private List<City> cities;
	private Integer[] order;
	private Double distance;
	public static final Random rand = new Random();

	public Path(Integer[] order, List<City> cities) throws TSPException {
		this.distance = -distance(cities, order);
		this.order = order;
		this.cities = cities;
	}

	@Override
	public int compareTo(Path other) {
		return this.getDistance().compareTo(other.getDistance());
	}

	public List<City> getCities() {
		return cities;
	}

	public Double getDistance() {
		return this.distance;
	}

	public Integer[] getOrder() {
		return this.order;
	}

	public static final double distance(List<City> cities, Integer[] order) throws TSPException {
		double totalDistance = 0;

		if (cities.size() != order.length) {
			throw new TSPException();
		}

		for (int i = 0; i < order.length - 1; ++i) {
			totalDistance += cities.get(order[i]).distance(cities.get(order[i + 1]));
		}

		totalDistance += cities.get(order[order.length - 1]).distance(cities.get(order[0]));

		return totalDistance;
	}

	public Integer[] getOrderDeepCopy() {
		Integer[] temp = new Integer[this.order.length];
		for (int i = 0; i < this.order.length; ++i) {
			temp[i] = this.order[i];
		}
		return temp;
	}

	public Path pathDeepCopy() throws TSPException {
		return new Path(this.getOrderDeepCopy(), this.cities);
	}

	private static Integer[] randArray(int numCities) {
		Integer[] intArray = new Integer[numCities];
		for (int i = 0; i < numCities; ++i) {
			intArray[i] = i;
		}
		List<Integer> intList = Arrays.asList(intArray);

		Collections.shuffle(intList);

		for (int i = 0; i < numCities; ++i) {
			intArray[i] = intList.get(i);
		}

		return intArray;
	}

	public static Path generateRandomPath(List<City> cities) throws TSPException {
		return new Path(randArray(cities.size()), cities);
	}

	public static Path getNeighbor(Path r) throws TSPException {
		Integer[] order = r.getOrderDeepCopy();
		int index1 = 0;
		int index2 = 0;
		while (index1 == index2) {
			index1 = rand.nextInt(order.length);
			index2 = rand.nextInt(order.length);
		}

		int temp = order[index2];
		order[index2] = order[index1];
		order[index1] = temp;

		return new Path(order, r.getCities());
	}

	public String printDistance() {
		return String.format("%.2f", -this.getDistance()) + "\n";
	}

}
