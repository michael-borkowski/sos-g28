package g28.sos1;

import g28.sos1.eval.EvaluationInstance;
import g28.sos1.eval.EvaluationInstanceResult;
import g28.sos1.eval.EvaluationSeries;
import g28.sos1.problems.LongestPathProblem;
import g28.sos1.problems.PathProblem;
import g28.sos1.problems.ShortestPathProblem;
import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Node;
import g28.sos1.problems.graphs.Path;
import g28.sos1.problems.graphs.PathGenerator;
import g28.sos1.solvers.Solver;
import g28.sos1.solvers.aco.AntColonySolver;
import g28.sos1.solvers.bruteforce.BruteForceSolver;
import g28.sos1.solvers.ga.GeneticAlgorithmSolver;
import g28.sos1.solvers.random.RandomSolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static g28.sos1.util.Distributions.uniform;

@SuppressWarnings("Duplicates")
public class MainOld {
    public static void main(String[] args) throws Exception {

    }

    private static void testGA_(PathProblem problem) {
        RandomSolver randomSolver = new RandomSolver();
        BruteForceSolver bruteForceSolver = new BruteForceSolver();
        AntColonySolver antColonySolver = new AntColonySolver();
        GeneticAlgorithmSolver geneticAlgorithmSolver = new GeneticAlgorithmSolver();
        long t;

        System.out.print("Random... ");
        t = System.currentTimeMillis();
        Path pRandom = randomSolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.print("Brute Force... ");
        t = System.currentTimeMillis();
        Path pBrute = bruteForceSolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.print("Ant Colony Optimization... ");
        t = System.currentTimeMillis();
        Path pAnt = antColonySolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.print("Genetic Algorithm... ");
        t = System.currentTimeMillis();
        Path pGA = geneticAlgorithmSolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.println("Random: " + pRandom);
        System.out.println("BF:     " + pBrute);
        System.out.println("ACO:    " + pAnt);
        System.out.println("GA:     " + pGA);
    }

    private static void repeatedComparison() {
        repeatedComparisonX(10, new BruteForceSolver(), new AntColonySolver());
    }

    @SafeVarargs
    private static void repeatedComparisonX(int iterations, Solver<PathProblem, Path>... solvers) {
        PathGenerator pathGenerator = new PathGenerator(10, 0.8, uniform(1, 100));
        List<DirectedGraph> paths = pathGenerator.generate(iterations);

        List<EvaluationSeries.Result> resultsLPP = new LinkedList<>();
        List<EvaluationSeries.Result> resultsSPP = new LinkedList<>();

        for (Solver<PathProblem, Path> solver : solvers) {
            System.out.println(solver + "...");
            List<EvaluationInstance> instances = new LinkedList<>();
            for(int i = 0; i < iterations; i++) instances.add(new EvaluationInstance(new LongestPathProblem(paths.get(i)), solver));
            EvaluationSeries series = new EvaluationSeries(instances);

            EvaluationSeries.Result result = series.runAll(System.out::println);
            resultsLPP.add(result);
        }

        for (Solver<PathProblem, Path> solver : solvers) {
            System.out.println(solver + "...");
            List<EvaluationInstance> instances = new LinkedList<>();
            for(int i = 0; i < iterations; i++) instances.add(new EvaluationInstance(new ShortestPathProblem(paths.get(i)), solver));
            EvaluationSeries series = new EvaluationSeries(instances);

            EvaluationSeries.Result result = series.runAll(System.out::println);
            resultsSPP.add(result);
        }

        System.out.println("\n=========================== SPP");

        for (int i = 0; i < resultsSPP.size(); i++) {
            EvaluationSeries.Result result = resultsSPP.get(i);
            System.out.println("\n" + solvers[i]);
            System.out.println(result);
        }

        System.out.println("\n=========================== LPP");

        for (int i = 0; i < resultsLPP.size(); i++) {
            EvaluationSeries.Result result = resultsLPP.get(i);
            System.out.println("\n" + solvers[i]);
            System.out.println(result);
        }
    }

    private static void test1() {
        PathGenerator pathGenerator = new PathGenerator(8, 0.8, uniform(0, 10));
        DirectedGraph dg = pathGenerator.generate();
        System.out.println(dg.getLongString());

        BruteForceSolver bruteForceSolver = new BruteForceSolver();
        AntColonySolver antColonySolver = new AntColonySolver();

        EvaluationInstance baseline = new EvaluationInstance(new LongestPathProblem(dg), bruteForceSolver);
        EvaluationInstance aco = new EvaluationInstance(new LongestPathProblem(dg), antColonySolver);

        EvaluationInstanceResult rBaseline = baseline.execute();
        System.out.println(rBaseline.getSolution().getDistance());

        EvaluationInstanceResult rACO = aco.execute();

        System.out.println("Duration BL:  " + rBaseline.getDuration());
        System.out.println("Duration ACO: " + rACO.getDuration());

        System.out.println("Path BL:  " + rBaseline.getSolution().getDistance());
        System.out.println("Path ACO: " + rACO.getSolution().getDistance());
    }

    private static void generatedN(int n, Solver<PathProblem, Path> solver) {
        PathGenerator pathGenerator = new PathGenerator(12, 0.5, uniform(0, 10));
        List<PathProblem> dg = pathGenerator.generate(n).stream().map(LongestPathProblem::new).collect(Collectors.toList());

        EvaluationSeries series = new EvaluationSeries(dg.stream()
                .map(solveWith(solver)).collect(Collectors.toList()));

        EvaluationSeries.Result result = series.runAll();

        System.out.println(result);
    }

    private static Function<? super PathProblem, EvaluationInstance> solveWith(Solver<PathProblem, Path> solver) {
        return x -> new EvaluationInstance(x, solver);
    }

    private static void generated1() {
        PathGenerator pathGenerator = new PathGenerator(10, 0.5, uniform(0, 10));
        DirectedGraph dg = pathGenerator.generate();

        System.out.println(dg.getLongString());

        System.out.println("=== BRUTE FORCE ===");
        BruteForceSolver bruteForceSolver = new BruteForceSolver();
        EvaluationInstance evaluationInstance = new EvaluationInstance(new LongestPathProblem(dg), bruteForceSolver);
        EvaluationInstanceResult result = evaluationInstance.execute();
        System.out.println(result.getDuration() + " ms, distance " + result.getSolution().getDistance());
        result = evaluationInstance.execute();
        System.out.println(result.getDuration() + " ms, distance " + result.getSolution().getDistance());
    }

    private static void manual() {
        System.out.println("Musst schaun");

        DirectedGraph dg = new DirectedGraph();

        Node A = dg.addNode("A");
        Node B = dg.addNode("B");
        Node C = dg.addNode("C");
        Node D = dg.addNode("D");

        dg.addEdge(A, B, 1);
        dg.addEdge(B, C, 1);
        dg.addEdge(C, A, 1);
        dg.addEdge(C, D, 1);
        dg.addEdge(B, D, 3);

        System.out.println(dg.getLongString());

        System.out.println("=== BRUTE FORCE ===");
        BruteForceSolver bruteForceSolver = new BruteForceSolver();
        Path p = bruteForceSolver.solve(new LongestPathProblem(dg));
        System.out.println(p);
    }
}
