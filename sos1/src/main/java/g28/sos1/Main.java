package g28.sos1;

import g28.sos1.problems.LongestPathProblem;
import g28.sos1.problems.PathProblem;
import g28.sos1.problems.ShortestPathProblem;
import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Path;
import g28.sos1.problems.graphs.PathGenerator;
import g28.sos1.solvers.aco.AntColonySolver;
import g28.sos1.solvers.bruteforce.BruteForceSolver;
import g28.sos1.solvers.ga.GeneticAlgorithmSolver;
import g28.sos1.solvers.random.RandomSolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static g28.sos1.util.Distributions.uniform;

@SuppressWarnings("Duplicates")
public class Main {
    private static final String csvHeader = "x,lpp,lpp-t,spp,spp-t";
    private static final int N = 10;
    private static final PathGenerator graphGenerator = new PathGenerator(30, 0.9, uniform(1, 100));

    public static void main(String[] args) throws Exception {
        System.out.println("============== A");
        //testPaper_2_GA_GenerationSize();
        System.out.println("============== B");
        //testPaper_3_GA_CombinationRate();
        System.out.println("============== C");
        //testPaper_4_GA_MutationRate();
        System.out.println("============== D");
        //testPaper_5_GA_Iterations();


        System.out.println("============== E");
        //testPaper_6_ACO_Ants();
        System.out.println("============== E-b");
        //testPaper_6b_ACO_Evaporation();

        System.out.println("============== D2");
        testPaper_5_GA_Iterations();

        Main2_Main.main(args);

        System.out.println("============== F");
        testPaper_7_ACO_Iterations();

    }

    private static void testPaper_7_ACO_Iterations() throws IOException {
        AntColonySolver antColonySolver = new AntColonySolver();

        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/testPaper_7_ACO_IterationsB.csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println(csvHeader);

        long t0 = System.currentTimeMillis();
        int iMin = 7501; // 1
        int iMax = 10000;
        for (int i = iMin; i <= iMax; i += 100) {
            antColonySolver.setIterations(i);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = graphGenerator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p = antColonySolver.solve(lpp);
                t = System.currentTimeMillis() - t;

                avg_lpp += (double) p.getDistance() / N;
                avg_lpp_t += (double) t / N;

                t = System.currentTimeMillis();
                p = antColonySolver.solve(spp);
                t = System.currentTimeMillis() - t;

                avg_spp += (double) p.getDistance() / N;
                avg_spp_t += (double) t / N;
            }

            pw.println(i + "," + avg_lpp + "," + avg_lpp_t + "," + avg_spp + "," + avg_spp_t);
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void testPaper_6b_ACO_Evaporation() throws IOException {
        AntColonySolver antColonySolver = new AntColonySolver();

        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/testPaper_6b_ACO_Evaporation.csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println(csvHeader);

        long t0 = System.currentTimeMillis();
        int iMin = 0;
        int iMax = 100;
        for (int i = iMin; i <= iMax; i += 1) {
            antColonySolver.setEvaporationRate((double) i / 100);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = graphGenerator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p = antColonySolver.solve(lpp);
                t = System.currentTimeMillis() - t;

                avg_lpp += (double) p.getDistance() / N;
                avg_lpp_t += (double) t / N;

                t = System.currentTimeMillis();
                p = antColonySolver.solve(spp);
                t = System.currentTimeMillis() - t;

                avg_spp += (double) p.getDistance() / N;
                avg_spp_t += (double) t / N;
            }

            pw.println((double) i / 100 + "," + avg_lpp + "," + avg_lpp_t + "," + avg_spp + "," + avg_spp_t);
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void testPaper_6_ACO_Ants() throws IOException {
        AntColonySolver antColonySolver = new AntColonySolver();

        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/testPaper_6_ACO_Ants.csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println(csvHeader);

        long t0 = System.currentTimeMillis();
        int iMin = 1;
        int iMax = 100;
        for (int i = iMin; i <= iMax; i += 1) {
            antColonySolver.setAnts(i);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = graphGenerator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p = antColonySolver.solve(lpp);
                t = System.currentTimeMillis() - t;

                avg_lpp += (double) p.getDistance() / N;
                avg_lpp_t += (double) t / N;

                t = System.currentTimeMillis();
                p = antColonySolver.solve(spp);
                t = System.currentTimeMillis() - t;

                avg_spp += (double) p.getDistance() / N;
                avg_spp_t += (double) t / N;
            }

            pw.println(i + "," + avg_lpp + "," + avg_lpp_t + "," + avg_spp + "," + avg_spp_t);
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void testPaper_5_GA_Iterations() throws IOException {
        GeneticAlgorithmSolver geneticAlgorithmSolver = new GeneticAlgorithmSolver();

        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/testPaper_5_GA_Iterations.csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println(csvHeader);

        long t0 = System.currentTimeMillis();
        int iMin = 5;
        int iMax = 1000;
        for (int i = iMin; i <= iMax; i += 10) {
            geneticAlgorithmSolver.setIterations(i);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = graphGenerator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p = geneticAlgorithmSolver.solve(lpp);
                t = System.currentTimeMillis() - t;

                avg_lpp += (double) p.getDistance() / N;
                avg_lpp_t += (double) t / N;

                t = System.currentTimeMillis();
                p = geneticAlgorithmSolver.solve(spp);
                t = System.currentTimeMillis() - t;

                avg_spp += (double) p.getDistance() / N;
                avg_spp_t += (double) t / N;
            }

            pw.println(i + "," + avg_lpp + "," + avg_lpp_t + "," + avg_spp + "," + avg_spp_t);
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void testPaper_4_GA_MutationRate() throws IOException {
        GeneticAlgorithmSolver geneticAlgorithmSolver = new GeneticAlgorithmSolver();

        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/testPaper_4_GA_MutationRate.csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println(csvHeader);

        long t0 = System.currentTimeMillis();
        int iMin = 0;
        int iMax = 100;
        for (int i = iMin; i <= iMax; i += 1) {
            geneticAlgorithmSolver.setMutationRate((double) i / 100);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = graphGenerator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p = geneticAlgorithmSolver.solve(lpp);
                t = System.currentTimeMillis() - t;

                avg_lpp += (double) p.getDistance() / N;
                avg_lpp_t += (double) t / N;

                t = System.currentTimeMillis();
                p = geneticAlgorithmSolver.solve(spp);
                t = System.currentTimeMillis() - t;

                avg_spp += (double) p.getDistance() / N;
                avg_spp_t += (double) t / N;
            }

            pw.println(((double) i / 100) + "," + avg_lpp + "," + avg_lpp_t + "," + avg_spp + "," + avg_spp_t);
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void testPaper_3_GA_CombinationRate() throws IOException {
        GeneticAlgorithmSolver geneticAlgorithmSolver = new GeneticAlgorithmSolver();

        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/testPaper_3_GA_CombinationRate.csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println(csvHeader);

        long t0 = System.currentTimeMillis();
        int iMin = 0;
        int iMax = 100;
        for (int i = iMin; i <= iMax; i += 1) {
            geneticAlgorithmSolver.setCombinationRate((double) i / 100);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = graphGenerator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p = geneticAlgorithmSolver.solve(lpp);
                t = System.currentTimeMillis() - t;

                avg_lpp += (double) p.getDistance() / N;
                avg_lpp_t += (double) t / N;

                t = System.currentTimeMillis();
                p = geneticAlgorithmSolver.solve(spp);
                t = System.currentTimeMillis() - t;

                avg_spp += (double) p.getDistance() / N;
                avg_spp_t += (double) t / N;
            }

            pw.println(((double) i / 100) + "," + avg_lpp + "," + avg_lpp_t + "," + avg_spp + "," + avg_spp_t);
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void testPaper_2_GA_GenerationSize() throws IOException {
        GeneticAlgorithmSolver geneticAlgorithmSolver = new GeneticAlgorithmSolver();

        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/testPaper_2_GA_GenerationSize.csv");
        PrintWriter pw = new PrintWriter(out);

        pw.println(csvHeader);
        long t0 = System.currentTimeMillis();
        int iMin = 1;
        int iMax = 200;
        for (int i = iMin; i <= iMax; i += 2) {
            geneticAlgorithmSolver.setGenerationSize(i);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = graphGenerator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p = geneticAlgorithmSolver.solve(lpp);
                t = System.currentTimeMillis() - t;

                avg_lpp += (double) p.getDistance() / N;
                avg_lpp_t += (double) t / N;

                t = System.currentTimeMillis();
                p = geneticAlgorithmSolver.solve(spp);
                t = System.currentTimeMillis() - t;

                avg_spp += (double) p.getDistance() / N;
                avg_spp_t += (double) t / N;
            }

            pw.println(i + "," + avg_lpp + "," + avg_lpp_t + "," + avg_spp + "," + avg_spp_t);
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void testPaper_1_Prelim() {
        PathGenerator graphGenerator = new PathGenerator(6, 0.7, uniform(1, 100));
        DirectedGraph graph = graphGenerator.generate();

//        System.out.println(graph.getLongString());

        PathProblem spp = new ShortestPathProblem(graph);
        PathProblem lpp = new LongestPathProblem(graph);

        System.out.println("================= SPP =================");
        testGA_(spp);
        System.out.println("================= LPP =================");
        testGA_(lpp);
    }

    private static void testGA_(PathProblem problem) {
        RandomSolver randomSolver = new RandomSolver();
        BruteForceSolver bruteForceSolver = new BruteForceSolver();
        AntColonySolver antColonySolver = new AntColonySolver();
        GeneticAlgorithmSolver geneticAlgorithmSolver = new GeneticAlgorithmSolver();
        long t;

        System.out.print("Random... ");
        t = System.currentTimeMillis();
        Path pRandom = randomSolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.print("Brute Force... ");
        t = System.currentTimeMillis();
        Path pBrute = bruteForceSolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.print("Ant Colony Optimization... ");
        t = System.currentTimeMillis();
        Path pAnt = antColonySolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.print("Genetic Algorithm... ");
        t = System.currentTimeMillis();
        Path pGA = geneticAlgorithmSolver.solve(problem);
        System.out.println("(" + (System.currentTimeMillis() - t) + " ms)");

        System.out.println("Random: " + pRandom);
        System.out.println("BF:     " + pBrute);
        System.out.println("ACO:    " + pAnt);
        System.out.println("GA:     " + pGA);
    }
}
