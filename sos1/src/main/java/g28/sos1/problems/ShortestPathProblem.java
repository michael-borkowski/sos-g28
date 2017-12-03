package g28.sos1.problems;

import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Path;
import g28.sos1.problems.graphs.criteria.ShortestPathCriterion;

public class ShortestPathProblem extends PathProblem {
    public ShortestPathProblem(DirectedGraph graph) {
        super(graph, new ShortestPathCriterion());
    }

    @Override
    public boolean accepts(Path path) {
        return path.getEdges().size() >= getGraph().getNodes().size() - 1 && super.accepts(path);
    }
}
