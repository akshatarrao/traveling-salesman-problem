
package TSPSolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import TSPSolver.Algorithms.GenericTSPAlgorithm;
import TSPSolver.Algorithms.RandomSearch;
import TSPSolver.Algorithms.SimulatedAnnealing;
import TSPSolver.Exception.TSPException;
import TSPSolver.models.City;
import TSPSolver.models.Path;

public class Main {

    public static void main(String[] args) throws Exception {

        GenericTSPAlgorithm alg = null;
        String algLabel = "";

        if (args.length == 0) {
            throw new TSPException("Did not specify the algorithm to use and the input .tsp file");
        } else {
            if (args[0].equalsIgnoreCase("sa")) {
                alg = new SimulatedAnnealing();
                algLabel = "Simulated Annealing";
            } else if (args[0].equalsIgnoreCase("rs")) {
                alg = new RandomSearch();
                algLabel = "Random Search";

            } else {
                throw new TSPException("Unknown algorithm");
            }
        }

        if (args.length == 1) {
            throw new TSPException("Did not specify the input .tsp file");
        } else {

            for (int i = 1; i < args.length; ++i) {
                String arg = args[i];
                if (!arg.endsWith(".tsp")) {
                    throw new TSPException("Input data file should be in .tsp format");
                }
                String filePath = System.getProperty("user.dir") + "/data/" + arg;
                File inputFile = new File(filePath);

                BufferedReader br = new BufferedReader(new FileReader(inputFile));

                String line = null;

                ArrayList<City> cities = new ArrayList<City>();

                // information that need to learn from the input file
                int dimension = -1;

                // load the input file, raise an exception in case of any error
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("TYPE")) {
                        line = line.replaceAll("\\s+", "");
                        String[] splitedLine = line.split(":");
                        if (!splitedLine[splitedLine.length - 1].equals("TSP")) {
                            br.close();
                            throw new Exception();
                        }
                    } else if (line.startsWith("DIMENSION")) {
                        line = line.replaceAll("\\s+", "");
                        String[] splitedLine = line.split(":");
                        try {
                            dimension = Integer.parseInt(splitedLine[splitedLine.length - 1]);
                        } catch (Exception e) {
                            br.close();
                            throw new Exception();
                        }
                    } else if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                        line = line.replaceAll("\\s+", "");
                        String[] splitedLine = line.split(":");
                        if (!splitedLine[splitedLine.length - 1].equals("EUC_2D")) {
                            br.close();
                            throw new Exception();
                        }
                    } else if (line.equals("NODE_COORD_SECTION")) {
                        if (dimension < 0) {
                            br.close();
                            throw new Exception();
                        }

                        int count = 0;
                        while ((count++) < dimension && ((line = br.readLine()) != null)) {
                            String[] splitedLine = line.trim().split("\\s+");
                            if (splitedLine.length != 3) {
                                br.close();
                                throw new Exception();
                            }
                            try {
                                City newCity = new City(Double.parseDouble(splitedLine[1]),
                                        Double.parseDouble(splitedLine[2]));
                                cities.add(newCity);
                            } catch (Exception e) {
                                br.close();
                                throw new Exception();
                            }
                        }
                        break;
                    }
                }

                // solve the TSP problem on the given set of nodes
                solveTSP(cities, alg);

                City.resetCounter();
                br.close();

            }
        }
    }

    private static void solveTSP(ArrayList<City> cities, GenericTSPAlgorithm algo) throws Exception {

        Path geneticAlgorithmSol = algo.solveTSP(cities);
        System.out.println(geneticAlgorithmSol.printDistance());

        // push the order of nodes into solution.csv
        Integer[] order = geneticAlgorithmSol.getOrder();
        List<Integer> dataLines = new ArrayList<>();
        for (int i = 0; i < order.length; ++i) {
            dataLines.add(order[i] + 1);
        }
        String path = System.getProperty("user.dir") + "/data/";
        File csvOutputFile = new File(path + "solution.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(e -> e.toString())
                    .forEach(pw::println);
        }
    }
}