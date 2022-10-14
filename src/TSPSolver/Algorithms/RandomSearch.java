package TSPSolver.Algorithms;

import java.util.List;

import TSPSolver.models.City;
import TSPSolver.models.Path;

public class RandomSearch implements GenericTSPAlgorithm {

    private static int numOfIter = 100;

    @Override
    public Path solveTSP(List<City> cities) throws Exception {

        Path optimalPath = Path.generateRandomPath(cities);

        for (int i = 0; i < numOfIter; i++) {
            Path temp = Path.generateRandomPath(cities);
            if (temp.getDistance() < optimalPath.getDistance())
                optimalPath = temp;
        }
        return optimalPath;
    }
}
