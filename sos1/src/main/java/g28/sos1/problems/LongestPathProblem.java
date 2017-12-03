package g28.sos1.problems;

import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.criteria.LongestPathCriterion;

public class LongestPathProblem extends PathProblem {
    public LongestPathProblem(DirectedGraph graph) {
        super(graph, new LongestPathCriterion());
    }
}
