package g28.sos1.problems.graphs.criteria;

import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Path;

public class ShortestPathCriterion extends PathCriterion {
    private long maxDistance = 0;

    @Override
    public void init(DirectedGraph graph) {
        maxDistance = graph.getNodes().size() * graph.getEdges().stream().mapToLong(Edge::getDistance).max().orElseThrow(IllegalStateException::new);
    }

    @Override
    public Double rateEdge(Edge edge, long pheromones) {
        return (double) pheromones; // + edge.getDistance();
    }

    @Override
    public double ratePath(Path path) {
        return (double) maxDistance - (1 + path.getDistance());
    }

    @Override
    public int compare(Path o1, Path o2) {
        if (o1.getEdges().size() != o2.getEdges().size())
            return -Integer.compare(o1.getEdges().size(), o2.getEdges().size());
        else return super.compare(o1, o2);
    }
}
