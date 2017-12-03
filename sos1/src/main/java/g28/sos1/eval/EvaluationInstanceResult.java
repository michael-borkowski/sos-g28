package g28.sos1.eval;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.Path;
import g28.sos1.solvers.Solver;

public class EvaluationInstanceResult {
    private final PathProblem problem;
    private final Solver<PathProblem, Path> solver;

    private final long duration;
    private final Path solution;

    public EvaluationInstanceResult(PathProblem problem, Solver<PathProblem, Path> solver, long duration, Path solution) {
        this.problem = problem;
        this.solver = solver;
        this.duration = duration;
        this.solution = solution;
    }

    public PathProblem getProblem() {
        return problem;
    }

    public Solver<PathProblem, Path> getSolver() {
        return solver;
    }

    public long getDuration() {
        return duration;
    }

    public Path getSolution() {
        return solution;
    }
}
