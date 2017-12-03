package g28.sos1.solvers.bruteforce;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Node;
import g28.sos1.problems.graphs.Path;
import g28.sos1.solvers.Solver;

import java.util.LinkedList;
import java.util.List;

public class BruteForceSolver implements Solver<PathProblem, Path> {
    @Override
    public Path solve(PathProblem problem) {
        List<Node> possibleStartNodes = problem.getGraph().getNodes();

        State state = new State(problem);

        for (Node node : possibleStartNodes)
            getBestPathFrom(node, state, new LinkedList<>(), 0);

        return state.bestSolution;
    }

    private List<Edge> extend(List<Edge> list, Edge edge) {
        LinkedList<Edge> newList = new LinkedList<>(list);
        newList.add(edge);
        return newList;
    }

    private void getBestPathFrom(Node node, State state, List<Edge> edges, long currentDistance) {
        if (!edges.isEmpty()) state.add(new Path(node.getParent(), edges));

        for (Edge e : node.getOutgoingEdges())
            if (!edgesContain(edges, e.getB()))
                getBestPathFrom(e.getB(), state, extend(edges, e), currentDistance + e.getDistance());
    }

    private boolean edgesContain(List<Edge> edges, Node node) {
        return edges.stream().anyMatch(e -> e.getA().equals(node) || e.getB().equals(node));
    }

    private class State {
        private final PathProblem problem;

        Path bestSolution;

        public State(PathProblem problem) {
            this.problem = problem;
        }

        void add(Path path) {
            if (problem.accepts(path) && (bestSolution == null || problem.getCriterion().compare(bestSolution, path) > 0))
                bestSolution = path;
        }
    }
}
