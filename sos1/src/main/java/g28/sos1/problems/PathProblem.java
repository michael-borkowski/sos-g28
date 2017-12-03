package g28.sos1.problems;

import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Node;
import g28.sos1.problems.graphs.Path;
import g28.sos1.problems.graphs.criteria.PathCriterion;

public abstract class PathProblem {
    private final PathCriterion criterion;
    private final DirectedGraph graph;

    protected PathProblem(DirectedGraph graph, PathCriterion criterion) {
        this.graph = graph;
        this.criterion = criterion;

        criterion.init(graph);
    }

    public PathCriterion getCriterion() {
        return criterion;
    }

    public DirectedGraph getGraph() {
        return graph;
    }

    public boolean accepts(Path path) {
        Node previousB = null;
        for (Edge edge : path.getEdges()) {
            if (graph.getOutgoingEdges(edge.getA()).stream().noneMatch(x -> x.getB().equals(edge.getB()))) return false;
            if (previousB != null && !previousB.equals(edge.getA())) return false;
            previousB = edge.getB();
        }
        return true;
    }
}
