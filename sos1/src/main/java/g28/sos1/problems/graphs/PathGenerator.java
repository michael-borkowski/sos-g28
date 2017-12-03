package g28.sos1.problems.graphs;

import g28.sos1.util.Distribution;
import g28.sos1.util.MyRandom;

import java.util.*;

public class PathGenerator {
    private int nodeCount;
    private double edgeDensity;
    private Distribution distanceDistribution;
    private boolean guaranteedCircle;

    public PathGenerator(int nodeCount, double edgeDensity, Distribution distanceDistribution) {
        this.nodeCount = nodeCount;
        this.edgeDensity = edgeDensity;
        this.distanceDistribution = distanceDistribution;
    }

    public boolean isGuaranteedCircle() {
        return guaranteedCircle;
    }

    public void setGuaranteedCircle(boolean guaranteedCircle) {
        this.guaranteedCircle = guaranteedCircle;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public double getEdgeDensity() {
        return edgeDensity;
    }

    public void setEdgeDensity(double edgeDensity) {
        this.edgeDensity = edgeDensity;
    }

    public Distribution getDistanceDistribution() {
        return distanceDistribution;
    }

    public void setDistanceDistribution(Distribution distanceDistribution) {
        this.distanceDistribution = distanceDistribution;
    }

    public DirectedGraph generate() {
        DirectedGraph graph = new DirectedGraph();
        for (long i = 0; i < nodeCount; i++) graph.addNode(String.valueOf(i + 1));
        Random random = MyRandom.getRandom();

        while (graph.getEdges().size() == 0 || (guaranteedCircle && !hasCircle(graph)))
            for (Node a : graph.getNodes())
                for (Node b : graph.getNodes())
                    if (a != b && random.nextDouble() < edgeDensity && !graph.hasEdge(a, b)) graph.addEdge(a, b, distanceDistribution.take());

        return graph;
    }

    private boolean hasCircle(DirectedGraph graph) {
        Node root = graph.getNodes().get(0);
        Set<Node> visited = new HashSet<>();

        visited.add(root);
        return checkSub(root, visited);
    }

    private boolean checkSub(Node node, Set<Node> visited) {
        if(visited.size() == node.getParent().getNodes().size()) return true;

        for (Edge edge : node.getOutgoingEdges()) {
            if(visited.contains(edge.getB())) continue;

            Set<Node> newVisited = new HashSet<>(visited);
            newVisited.add(edge.getB());

            if(checkSub(edge.getB(), newVisited)) return true;
        }

        return false;
    }

    public List<DirectedGraph> generate(int count) {
        List<DirectedGraph> problems = new LinkedList<>();
        for (int i = 0; i < count; i++) problems.add(generate());
        return problems;
    }
}
