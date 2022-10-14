package TSPSolver.Algorithms;

import java.util.List;

import TSPSolver.models.City;
import TSPSolver.models.Path;

public interface GenericTSPAlgorithm {

    public Path solveTSP(List<City> cities) throws Exception;

}
