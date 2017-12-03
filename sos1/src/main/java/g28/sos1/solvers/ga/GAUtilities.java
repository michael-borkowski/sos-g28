package g28.sos1.solvers.ga;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Node;
import g28.sos1.problems.graphs.Path;
import g28.sos1.util.MyRandom;

import java.util.*;

public class GAUtilities {
    private static final Random random = MyRandom.getRandom();

    public static Chromosome getRandomChromosome(PathProblem problem, Node[] graphNodes) {
        int[] nodes = new int[graphNodes.length];
        Path path;

        long timeLimit = System.currentTimeMillis() + 100;
        do {
            List<Integer> remainingNodes = new LinkedList<>();
            for (int i = 0; i < graphNodes.length; i++) remainingNodes.add(i);

            for (int j = 0; j < nodes.length; j++)
                nodes[j] = remainingNodes.remove(random.nextInt(remainingNodes.size()));

            path = tryCreatePath(graphNodes, nodes);
        } while (System.currentTimeMillis() < timeLimit && (path == null || !problem.accepts(path)));

        if(path == null) return null;
        return new Chromosome(nodes, path, problem.getCriterion().ratePath(path));
    }

    public static Path tryCreatePath(Node[] graphNodes, int[] nodes) {
        Node a = graphNodes[nodes[0]];
        Set<Node> visited = new HashSet<>();
        visited.add(a);

        List<Edge> edges = new LinkedList<>();
        for (int i = 1; i < nodes.length; i++) {
            Node b = graphNodes[nodes[i]];

            if (!visited.contains(b)) {
                Edge edge = a.getOutgoingEdges().stream().filter(x -> x.getB().equals(b)).findFirst().orElse(null);
                if (edge != null) edges.add(edge);
                else break;

                visited.add(a = b);
            }
        }

        if (edges.size() == 0) return null;
        else return new Path(edges.get(0).getA().getParent(), edges);
    }
}
