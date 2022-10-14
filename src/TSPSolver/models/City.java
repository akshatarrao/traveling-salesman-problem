package TSPSolver.models;

import java.lang.Math;

public class City {
    private static int counter = 1;
    private int label;
    private double x;
    private double y;

    public City(double x, double y) {
        this.x = x;
        this.y = y;
        this.label = counter++;
    }

    // reference http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp95.pdf
    // dij = nint( sqrt( (x[i] - x[j])^2 + (y[i] - y[j])^2) );
    public double distance(City dest) {
        return Math.sqrt(Math.pow(this.x - dest.x, 2) + Math.pow(this.y - dest.y, 2));
    }

    public static void resetCounter() {
        counter = 1;
    }

    @Override
    public String toString() {
        return Integer.toString(this.label) + " " + Double.toString(this.x) + " " + Double.toString(this.y);
    }

}
