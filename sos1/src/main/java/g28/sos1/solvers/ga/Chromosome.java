package g28.sos1.solvers.ga;

import g28.sos1.problems.graphs.Path;

public class Chromosome {
    public final int[] nodes;
    public final Path path;
    public final double fitness;

    public Chromosome(int[] nodes, Path path, double fitness) {
        this.nodes = nodes;
        this.path = path;
        this.fitness = fitness;
    }
}
