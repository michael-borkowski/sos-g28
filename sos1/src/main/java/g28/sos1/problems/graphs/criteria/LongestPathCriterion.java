package g28.sos1.problems.graphs.criteria;

import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Path;

public class LongestPathCriterion extends PathCriterion {
    private long maxDistance = 0;

    @Override
    public void init(DirectedGraph graph) {
        maxDistance = graph.getEdges().stream().mapToLong(Edge::getDistance).max().orElseThrow(IllegalStateException::new);
    }

    @Override
    public Double rateEdge(Edge edge, long pheromones) {
        return (double) maxDistance - pheromones; // + edge.getDistance();
    }

    @Override
    public double ratePath(Path path) {
        return path.getDistance();
    }
}
