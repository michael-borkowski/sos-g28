package g28.sos1.solvers.random;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.Node;
import g28.sos1.problems.graphs.Path;
import g28.sos1.solvers.Solver;
import g28.sos1.solvers.ga.Chromosome;

import static g28.sos1.solvers.ga.GAUtilities.getRandomChromosome;

public class RandomSolver implements Solver<PathProblem, Path> {
    private int iterations = 1;

    @Override
    public Path solve(PathProblem problem) {
        Chromosome best = null;

        if (iterations <= 0) return null;
        for (int i = 0; i < iterations; i++) {
            Chromosome currentChromosome = getRandomChromosome(problem, problem.getGraph().getNodes().toArray(new Node[0]));
            if (best == null || best.fitness < currentChromosome.fitness)
                best = currentChromosome;
        }

        return best.path;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
