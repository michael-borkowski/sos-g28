package g28.sos1.problems.graphs.criteria;

import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Path;

import java.util.Comparator;
import java.util.List;

public abstract class PathCriterion implements Comparator<Path> {
    public PathCriterion() {
    }

    public abstract void init(DirectedGraph graph);

    public abstract Double rateEdge(Edge edge, long pheromones);

    public abstract double ratePath(Path temporaryPath);

    public Path getBestPath(List<Path> paths) {
        return paths.stream().sorted(this).findFirst().orElse(null);
    }

    @Override
    public int compare(Path o1, Path o2) {
        return -Double.compare(ratePath(o1), ratePath(o2));
    }
}
