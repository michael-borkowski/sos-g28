package g28.sos1.solvers.ga;

import g28.sos1.problems.graphs.Path;

import java.util.Set;

public class Generation {
    private Set<Chromosome> solutions;

    public Generation(Set<Chromosome> solutions) {
        this.solutions = solutions;
    }

    public Set<Chromosome> getSolutions() {
        return solutions;
    }
}
