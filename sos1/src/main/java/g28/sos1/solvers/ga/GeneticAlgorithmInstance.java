package g28.sos1.solvers.ga;

import g28.sos1.problems.PathProblem;
import g28.sos1.problems.graphs.Node;
import g28.sos1.problems.graphs.Path;
import g28.sos1.util.MyRandom;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static g28.sos1.solvers.ga.GAUtilities.tryCreatePath;

public class GeneticAlgorithmInstance {
    private final PathProblem problem;
    private final long iterations;
    private final long generationSize;

    private final Random random = MyRandom.getRandom();

    private Node[] graphNodes;
    private final double combinationRate;
    private final double mutationRate;

    private Chromosome pocket = null;

    public GeneticAlgorithmInstance(PathProblem problem, long iterations, long generationSize, double combinationRate, double mutationRate) {
        this.problem = problem;
        this.iterations = iterations;
        this.generationSize = generationSize;

        this.graphNodes = problem.getGraph().getNodes().toArray(new Node[0]);
        this.combinationRate = combinationRate;
        this.mutationRate = mutationRate;
    }

    public Path solve() {
        Generation generation = initializeGeneration();
        if(generation.getSolutions().size() == 0) return null;

        for (long i = 0; i < iterations; i++) {
            Generation offspring = createOffspring(generation);
            generation = select(offspring);

            Chromosome currentWinner = sortChromosomes(generation).findFirst().orElse(null);
            if (currentWinner != null && (pocket == null || pocket.fitness < currentWinner.fitness))
                pocket = currentWinner;
        }

        if (pocket == null) return null;
        else return pocket.path;
    }

    private Generation initializeGeneration() {
        Set<Chromosome> solutions = new HashSet<>();
        long timeLimit = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < timeLimit && solutions.size() < generationSize) {
            Chromosome chromosome = GAUtilities.getRandomChromosome(problem, graphNodes);
            if(chromosome != null) solutions.add(chromosome);
        }
        return new Generation(solutions);
    }

    private Generation createOffspring(Generation generation) {
        Set<Chromosome> newGeneration = new HashSet<>();

        for (Chromosome chromosome : generation.getSolutions()) {
            if (generation.getSolutions().size() > 1) {
                int combinations = takeEvents(combinationRate);
                for (int i = 0; i < combinations; i++) {
                    Chromosome other = selectRandomOther(generation.getSolutions(), chromosome);
                    Chromosome child;
                    do {
                        child = combine(chromosome, other);
                    } while (child == null);
                    newGeneration.add(child);
                }
            }

            int mutations = takeEvents(mutationRate);
            for (int i = 0; i < mutations; i++) {
                Chromosome mutant;
                do {
                    mutant = mutate(chromosome);
                } while (mutant == null);
                newGeneration.add(mutant);
            }

            newGeneration.add(chromosome);
        }

        return new Generation(newGeneration);
    }

    private int takeEvents(double combinationRate) {
        return (int) (Math.floor(combinationRate) + (random.nextDouble() < (combinationRate % 1) ? 1 : 0));
    }

    private Chromosome mutate(Chromosome chromosome) {
        // 0: swap A <--> B
        // 1: reverse A--B
        // 2: invert A--B
        // 3: randomize A--B

        int type = random.nextInt(4);

        switch (type) {
            case 0:
                return mutateSwap(chromosome);
            case 1:
                return mutateReverse(chromosome);
            case 2:
                return mutateInvert(chromosome);
            case 3:
                return mutateRandomize(chromosome);
            default:
                throw new IllegalStateException();
        }
    }

    private Chromosome mutateSwap(Chromosome chromosome) {
        int a_ = random.nextInt(chromosome.nodes.length);
        int b_ = random.nextInt(chromosome.nodes.length);
        int a = Math.min(a_, b_);
        int b = Math.max(a_, b_);
        int[] nodes_ = chromosome.nodes.clone();

        nodes_[a] = chromosome.nodes[b];
        nodes_[b] = chromosome.nodes[a];

        Path path = tryCreatePath(graphNodes, nodes_);
        if (path != null) return new Chromosome(nodes_, path, problem.getCriterion().ratePath(path));
        else return null;
    }

    private Chromosome mutateReverse(Chromosome chromosome) {
        int a_ = random.nextInt(chromosome.nodes.length);
        int b_ = random.nextInt(chromosome.nodes.length);
        int a = Math.min(a_, b_);
        int b = Math.max(a_, b_);
        int[] nodes_ = chromosome.nodes.clone();

        for (int i = a; i <= b; i++) nodes_[i] = chromosome.nodes[b - (i - a)];

        Path path = tryCreatePath(graphNodes, nodes_);
        if (path != null) return new Chromosome(nodes_, path, problem.getCriterion().ratePath(path));
        else return null;
    }

    private Chromosome mutateInvert(Chromosome chromosome) {
        int a_ = random.nextInt(chromosome.nodes.length);
        int b_ = random.nextInt(chromosome.nodes.length);
        int a = Math.min(a_, b_);
        int b = Math.max(a_, b_);
        int[] nodes_ = chromosome.nodes.clone();

        for (int i = a; i <= b; i++) nodes_[i] = graphNodes.length - chromosome.nodes[i] - 1;

        Path path = tryCreatePath(graphNodes, nodes_);
        if (path != null) return new Chromosome(nodes_, path, problem.getCriterion().ratePath(path));
        else return null;
    }

    private Chromosome mutateRandomize(Chromosome chromosome) {
        int a_ = random.nextInt(chromosome.nodes.length);
        int b_ = random.nextInt(chromosome.nodes.length);
        int a = Math.min(a_, b_);
        int b = Math.max(a_, b_);
        int[] nodes_ = chromosome.nodes.clone();

        for (int i = a; i <= b; i++) nodes_[i] = random.nextInt(graphNodes.length);

        Path path = tryCreatePath(graphNodes, nodes_);
        if (path != null) return new Chromosome(nodes_, path, problem.getCriterion().ratePath(path));
        else return null;
    }

    private Chromosome combine(Chromosome a, Chromosome b) {
        int cut = random.nextInt(a.nodes.length);

        int[] nodes_ = a.nodes.clone();

        System.arraycopy(b.nodes, cut, nodes_, cut, a.nodes.length - cut);

        Path path = tryCreatePath(graphNodes, nodes_);
        if (path != null) return new Chromosome(nodes_, path, problem.getCriterion().ratePath(path));
        else return null;
    }

    private Generation select(Generation offspring) {
        return new Generation(
                sortChromosomes(offspring)
                        .limit(generationSize).collect(Collectors.toSet()));
    }

    private Stream<Chromosome> sortChromosomes(Generation generation) {
        return generation.getSolutions().stream().filter(x -> problem.accepts(x.path)).sorted(Comparator.comparingDouble(o -> -o.fitness));
    }

    private <O> O selectRandomOther(Set<O> objects, O current) {
        List<O> list = new LinkedList<>(objects);
        list.remove(current);
        return list.get(random.nextInt(list.size()));
    }
}
