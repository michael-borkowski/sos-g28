package g28.sos1.problems.graphs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Path {
    private final DirectedGraph graph;
    private final List<Edge> edges;

    public Path(DirectedGraph graph, List<Edge> edges) {
        this.graph = graph;
        this.edges = edges;

        if (edges.size() != 0) {
            Set<Node> nodes = new HashSet<>();
            Node n = edges.get(0).getA();
            nodes.add(n);
            for (Edge edge : edges)
                if (n != edge.getA())
                    throw new IllegalStateException("path edges not consecutive (" + n + " != " + edge.getA() + ")");
                else if (nodes.contains(edge.getB()))
                    throw new IllegalStateException("node " + edge.getB() + " visited twice");
                else nodes.add(n = edge.getB());
        }
    }

    public DirectedGraph getGraph() {
        return graph;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        if (edges.isEmpty()) return "(empty)";

        StringBuilder sb = new StringBuilder();

        sb.append(getDistance()).append(": ");
        sb.append(edges.get(0).getA());
        for (Edge edge : edges) sb.append(" -> (").append(edge.getDistance()).append(") ").append(edge.getB());
        return sb.toString();
    }

    public long getDistance() {
        return edges.stream().mapToLong(Edge::getDistance).sum();
    }
}
