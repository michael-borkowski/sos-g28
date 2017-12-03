package g28.sos1.problems.graphs;

import java.util.*;

public class DirectedGraph {
    private List<Node> nodes = new LinkedList<>();
    private Set<Edge> edges = new HashSet<>();

    private Map<Node, Set<Edge>> outgoing = new HashMap<>();

    public Node addNode(String name) {
        Node node = new Node(this, name);

        check(!this.nodes.contains(node), "Node " + node + " already contained");

        this.nodes.add(node);
        this.outgoing.put(node, new HashSet<>());

        return node;
    }

    public Edge addEdge(Node a, Node b, long distance) {
        Edge edge = new Edge(a, b, distance);

        check(this.nodes.contains(edge.getA()), "Node A (" + edge.getA() + ") not contained");
        check(this.nodes.contains(edge.getB()), "Node B (" + edge.getB() + ") not contained");
        check(!this.outgoing.get(edge.getA()).contains(edge), "Edge (" + edge + ") already contained");

        this.edges.add(edge);
        getOutgoingEdges(a).add(edge);

        return edge;
    }

    public Set<Edge> getOutgoingEdges(Node node) {
        return outgoing.get(node);
    }

    private void check(boolean b, String text) {
        if (!b) throw new IllegalStateException(text);
    }

    @Override
    public String toString() {
        return nodes.size() + " nodes, " + edges.size() + " edges";
    }

    public String getLongString() {
        StringBuilder sb = new StringBuilder();

        nodes.forEach(x -> sb.append(x).append(", "));
        if (!nodes.isEmpty()) sb.delete(sb.length() - 2, sb.length());
        sb.append("\n\n");

        for(Node n: nodes) {
            sb.append(n);
            sb.append('\n');
            getOutgoingEdges(n).forEach(x -> sb.append("  ").append(x).append('\n'));
            sb.append('\n');
        }
        return sb.toString();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public boolean hasEdge(Node a, Node b) {
        return getOutgoingEdges(a).stream().anyMatch(x -> x.getB().equals(b));
    }
}
