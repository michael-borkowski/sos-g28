package g28.sos1.solvers.aco;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Node;
import g28.sos1.problems.graphs.Path;
import g28.sos1.problems.graphs.criteria.PathCriterion;
import g28.sos1.util.MyRandom;

import java.util.*;
import java.util.stream.Collectors;

public class AntColonyInstance {
    private static final long PHEROMONE_ADDITION_FACTOR = 100;

    private final PathProblem problem;
    private final DirectedGraph graph;
    private final int ants;
    private final long iterations;
    private final double evaporationRate;
    private final PathCriterion criterion;

    private final Random random = MyRandom.getRandom();

    private Map<Edge, Long> pheromones = new HashMap<>();

    public AntColonyInstance(PathProblem problem, int ants, long iterations, double evaporationRate) {
        this.problem = problem;
        this.ants = ants;
        this.iterations = iterations;
        this.evaporationRate = evaporationRate;

        this.graph = problem.getGraph();
        this.criterion = problem.getCriterion();

        graph.getEdges().forEach(x -> pheromones.put(x, 0L));
    }

    public Path solve() {
        for (int iteration = 0; iteration < iterations; iteration++) {
            Map<Edge, Long> pheromoneDelta = new HashMap<>();
            for (int antIndex = 0; antIndex < ants; antIndex++) {
                Ant ant = new Ant(graph.getNodes().get(random.nextInt(graph.getNodes().size())));
                while (ant.isAlive()) moveAnt(ant);
                addPheromoneDelta(pheromoneDelta, ant.getHistory());
            }

            copyPheromones(pheromoneDelta);
        }

        //printDiag();

        return criterion.getBestPath(graph.getNodes().stream().map(this::followPheromones).filter(Objects::nonNull).filter(problem::accepts).collect(Collectors.toList()));
    }

    private void printDiag() {
        long maxPheromones = 1;
        for (Node node : graph.getNodes())
            for (Edge edge : node.getOutgoingEdges()) maxPheromones = Math.max(maxPheromones, pheromones.get(edge));

        for (Node node : graph.getNodes()) {
            System.out.println("\n" + node);
            for (Edge edge : node.getOutgoingEdges())
                System.out.printf("  %20s ## %10d (%6.2f%%)\n", edge.toString(), pheromones.get(edge), 100d * pheromones.get(edge) / maxPheromones);
        }
    }

    private void addPheromoneDelta(Map<Edge, Long> pheromoneDelta, List<Edge> history) {
        Path temporaryPath = new Path(graph, history);
        double pathRating = criterion.ratePath(temporaryPath);
        history.forEach(edge -> pheromoneDelta.put(edge, (long) (pathRating * PHEROMONE_ADDITION_FACTOR)));
    }

    private Path followPheromones(Node node) {
        List<Edge> edges = new LinkedList<>();
        List<Node> nodes = new LinkedList<>();

        nodes.add(node);

        while (true) {
            Edge edge = weigh(node.getOutgoingEdges()).entrySet().stream()
                    .filter(a -> !nodes.contains(a.getKey().getB()))
                    .sorted((a, b) -> -a.getValue().compareTo(b.getValue()))
                    .map(Map.Entry::getKey).findFirst().orElse(null);
            if (edge == null) break;

            edges.add(edge);
            nodes.add(edge.getB());
            node = edge.getB();
        }

        if (edges.isEmpty()) return null;
        else return new Path(graph, edges);
    }

    private void copyPheromones(Map<Edge, Long> pheromoneDelta) {
        for (Edge edge : pheromoneDelta.keySet())
            pheromones.put(edge, (long) (evaporationRate * (pheromones.get(edge) + pheromoneDelta.get(edge))));
    }

    private void moveAnt(Ant ant) {
        List<Edge> antHistory = ant.getHistory();
        Set<Edge> possibleEdges = ant.getPosition().getOutgoingEdges().stream()
                .filter(x -> !isInHistory(antHistory, x.getB()))
                .collect(Collectors.toSet());

        if (possibleEdges.isEmpty()) {
            ant.die();
        } else {
            Edge newEdge = selectWeighted(weigh(possibleEdges), random.nextDouble());
            ant.move(newEdge);
        }
    }

    private boolean isInHistory(List<Edge> history, Node node) {
        return history.stream().anyMatch(x -> x.getA().equals(node) || x.getB().equals(node));
    }

    private Edge selectWeighted(Map<Edge, Double> weightedEdges, double randomValue) {
        List<Double> limits = new LinkedList<>();
        Map<Double, Edge> limitEdges = new HashMap<>();
        double cumulative = 0;

        for (Edge edge : weightedEdges.keySet()) {
            cumulative += weightedEdges.get(edge);
            limits.add(cumulative);
            limitEdges.put(cumulative, edge);
        }

        randomValue /= cumulative;

        Edge lastGood = limitEdges.get(limits.get(0));
        for (double limit : limits) {
            if (randomValue < limit) break;
            else lastGood = limitEdges.get(limit);
        }
        return lastGood;
    }

    private Map<Edge, Double> weigh(Set<Edge> possibleEdges) {
        Map<Edge, Double> ret = new HashMap<>();
        possibleEdges.forEach(x -> ret.put(x, weigh(x)));

        return ret;
    }

    private Double weigh(Edge edge) {
        return criterion.rateEdge(edge, pheromones.get(edge));
    }
}
