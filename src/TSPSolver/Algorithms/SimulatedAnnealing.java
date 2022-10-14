package TSPSolver.Algorithms;

import java.util.List;
import java.util.Random;

import TSPSolver.Exception.TSPException;
import TSPSolver.models.City;
import TSPSolver.models.Path;

public class SimulatedAnnealing implements GenericTSPAlgorithm {

    private static int numOfIter = 100;
    private int temp = 1000;
    private double endTemp = 0.0001;
    private static double coolingFactor = 0.9999;
    private static final Random rand = new Random();

    private Path optimalPath;

    public void reset() {
        this.optimalPath = null;
    }

    public Path getOptimalPath() {
        return this.optimalPath;
    }

    private static double probability(double d1, double d2, double temp) {
        if (d1 > d2)
            return 1;
        else
            return Math.exp((d1 - d2) / temp);
    }

    public Path simulatedAnnealing(Path startPath) throws TSPException {

        double localCoolingFactor = coolingFactor;

        double localTemp = temp;
        Path currentPath = startPath;
        Path currentBestPath = null;

        while (localTemp > endTemp) {
            Path neighbor = Path.getNeighbor(currentPath);
            if (rand.nextDouble() < probability(-currentPath.getDistance(), -neighbor.getDistance(), localTemp)) {
                currentPath = neighbor;
            }

            if (currentBestPath == null || currentPath.compareTo(currentBestPath) > 0) {
                currentBestPath = currentPath.pathDeepCopy();
            }

            localTemp *= localCoolingFactor;
        }

        if (null != currentBestPath && (optimalPath == null || currentBestPath.compareTo(optimalPath) > 0))
            optimalPath = currentBestPath.pathDeepCopy();

        return currentBestPath;
    }

    @Override
    public Path solveTSP(List<City> cities) throws Exception {
        reset();

        Path startPath = simulatedAnnealing(Path.generateRandomPath(cities));

        for (int i = 1; i < numOfIter; ++i) {
            startPath = simulatedAnnealing(startPath);
        }

        return optimalPath;

    }

}
