package g28.sos1.eval;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Path;
import g28.sos1.solvers.Solver;

public class EvaluationInstance {
    private final PathProblem problem;
    private final Solver<PathProblem, Path> solver;

    public EvaluationInstance(PathProblem problem, Solver<PathProblem, Path> solver) {
        this.problem = problem;
        this.solver = solver;
    }

    public EvaluationInstanceResult execute() {
        long time = System.currentTimeMillis();
        Path solution = solver.solve(problem);
        time = System.currentTimeMillis() - time;

        if (solution == null) return null;
        else return new EvaluationInstanceResult(problem, solver, time, solution);
    }
}
