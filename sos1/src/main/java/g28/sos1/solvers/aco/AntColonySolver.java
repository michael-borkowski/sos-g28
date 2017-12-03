package g28.sos1.solvers.aco;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.Path;
import g28.sos1.solvers.Solver;

public class AntColonySolver implements Solver<PathProblem, Path> {
    private int ants = 60;
    private long iterations = 50;
    private double evaporationRate = 0.1;

    @Override
    public Path solve(PathProblem problem) {
        AntColonyInstance instance = new AntColonyInstance(problem, ants, iterations, evaporationRate);
        return instance.solve();
    }

    public int getAnts() {
        return ants;
    }

    public void setAnts(int ants) {
        this.ants = ants;
    }

    public long getIterations() {
        return iterations;
    }

    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    public double getEvaporationRate() {
        return evaporationRate;
    }

    public void setEvaporationRate(double evaporationRate) {
        this.evaporationRate = evaporationRate;
    }
}
