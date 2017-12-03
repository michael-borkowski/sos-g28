package g28.sos1.solvers.ga;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Path;
import g28.sos1.solvers.Solver;

public class GeneticAlgorithmSolver implements Solver<PathProblem, Path> {
    private long iterations = 500;
    private long generationSize = 100;
    private double combinationRate = 0.1;
    private double mutationRate = 0.5;

    public GeneticAlgorithmSolver() {
    }

    @Override
    public Path solve(PathProblem problem) {
        GeneticAlgorithmInstance instance = new GeneticAlgorithmInstance(problem, iterations, generationSize, combinationRate, mutationRate);
        return instance.solve();
    }

    public long getGenerationSize() {
        return generationSize;
    }

    public void setGenerationSize(long generationSize) {
        this.generationSize = generationSize;
    }

    public long getIterations() {
        return iterations;
    }

    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    public double getCombinationRate() {
        return combinationRate;
    }

    public void setCombinationRate(double combinationRate) {
        this.combinationRate = combinationRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}
