package g28.sos1;

import g28.sos1.problems.LongestPathProblem;
import g28.sos1.problems.PathProblem;
import g28.sos1.problems.ShortestPathProblem;
import g28.sos1.problems.graphs.DirectedGraph;
import g28.sos1.problems.graphs.Path;
import g28.sos1.problems.graphs.PathGenerator;
import g28.sos1.solvers.Solver;
import g28.sos1.solvers.aco.AntColonySolver;
import g28.sos1.solvers.bruteforce.BruteForceSolver;
import g28.sos1.solvers.ga.GeneticAlgorithmSolver;
import g28.sos1.util.Distribution;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static g28.sos1.util.Distributions.uniform;

@SuppressWarnings("Duplicates")
public class Main2_Main {
    private static final int N = 20;
    private static final double defaultDensity = 0.98;
    private static final Distribution defaultDistribution = uniform(0, 100);

    private static final int NODES_MAX = 20;
    private static final int NODES_MAX_BIG = 80;

    private static final int NODES_FIXED = 6;

    public static void main(String[] args) throws Exception {
        warmup();

		/* PERMANENTLY DISCARDED
        System.out.println("============== A");
        //m1_nodeCount(-1, "_ga", new GeneticAlgorithmSolver());
        System.out.println("============== B");
        //m1_nodeCount(-1, "_aco", new AntColonySolver());
		*/
        System.out.println("============== C");
        //m1_nodeCount(10, "_bf", new BruteForceSolver());


        System.out.println("============== F");
        m3_density("_ga", new GeneticAlgorithmSolver());
        System.out.println("============== G");
        m3_density("_aco", new AntColonySolver());
        System.out.println("============== H");
        m3_density("_bf", new BruteForceSolver());
		
        System.out.println("============== E");
        //m2_nodeCountBig("_aco", new AntColonySolver());
        System.out.println("============== D");
        //m2_nodeCountBig("_ga", new GeneticAlgorithmSolver());
    }

    private static void m3_density(String suffix, Solver<PathProblem, Path> solver) throws IOException {
        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/m3_density" + suffix + ".csv", false);
        PrintWriter pw = new PrintWriter(out);

        System.out.println("density...");
        pw.println("x,lpp,lpp-t,spp,spp-t");

        long t0 = System.currentTimeMillis();
        int iMin = 1;
        int iMax = 99;
        for (int i = iMin; i <= iMax; i += 1) {
            PathGenerator generator = new PathGenerator(NODES_FIXED, (double) (100 - i) / 100, defaultDistribution);
            generator.setGuaranteedCircle(true);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;
            boolean errorLpp = false, errorSpp = false;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = generator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p;
                int ret = 0;
                do {
                    p = solver.solve(lpp);
                } while (p == null && (++ret < 5));
                t = System.currentTimeMillis() - t;

                if (p == null) {
                    errorLpp = true;
                } else {
                    avg_lpp += (double) p.getDistance() / N;
                    avg_lpp_t += (double) t / N;
                }

                t = System.currentTimeMillis();
                ret = 0;
                do {
                    p = solver.solve(spp);
                } while (p == null && (++ret < 5));
                t = System.currentTimeMillis() - t;

                if (p == null) {
                    errorSpp = true;
                } else {
                    avg_spp += (double) p.getDistance() / N;
                    avg_spp_t += (double) t / N;
                }
            }

            pw.println(((double) (100 - i) / 100) + "," + (errorLpp ? "nan,nan" : avg_lpp + "," + avg_lpp_t) + "," + (errorSpp ? "nan,nan" : avg_spp + "," + avg_spp_t));
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void m2_nodeCountBig(String suffix, Solver<PathProblem, Path> solver) throws IOException {
        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/m2_nodeCountBig" + suffix + ".csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println("x,lpp,lpp-t,spp,spp-t");

        long t0 = System.currentTimeMillis();
        int iMin = 2;
        int iMax = NODES_MAX_BIG;
        for (int i = iMin; i <= iMax; i += 1) {
            PathGenerator generator = new PathGenerator(i, defaultDensity, defaultDistribution);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;
            boolean errorLpp = false, errorSpp = false;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = generator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p;
                int ret = 0;
                do {
                    p = solver.solve(lpp);
                } while (p == null && (++ret < 5));
                t = System.currentTimeMillis() - t;

                if (p == null) {
                    errorLpp = true;
                } else {
                    avg_lpp += (double) p.getDistance() / N;
                    avg_lpp_t += (double) t / N;
                }

                t = System.currentTimeMillis();
                ret = 0;
                do {
                    p = solver.solve(spp);
                } while (p == null && (++ret < 5));
                t = System.currentTimeMillis() - t;

                if (p == null) {
                    errorSpp = true;
                } else {
                    avg_spp += (double) p.getDistance() / N;
                    avg_spp_t += (double) t / N;
                }
            }

            pw.println(i + "," + (errorLpp ? "nan,nan" : avg_lpp + "," + avg_lpp_t) + "," + (errorSpp ? "nan,nan" : avg_spp + "," + avg_spp_t));
            pw.flush();
        }

        pw.close();
        out.close();
    }

    private static void m1_nodeCount(int limit, String suffix, Solver<PathProblem, Path> solver) throws IOException {
        FileWriter out = new FileWriter("/home/michael/research/sos/sos-1/results/m1_nodeCount" + suffix + ".csv", false);
        PrintWriter pw = new PrintWriter(out);

        pw.println("x,lpp,lpp-t,spp,spp-t");

        long t0 = System.currentTimeMillis();
        int iMin = 2;
        int iMax = NODES_MAX;
        if (limit >= 0 && limit < iMax) iMax = limit;
        for (int i = iMin; i <= iMax; i += 1) {
            PathGenerator generator = new PathGenerator(i, defaultDensity, defaultDistribution);

            if (i > iMin) {
                long t = System.currentTimeMillis() - t0;
                double progress = (double) (i - iMin) / (iMax - iMin);
                long eta = (long) ((double) t / progress);
                long duration = (eta - t) / 1000;

                if (duration > 60) System.out.println((duration / 60) + " min " + (duration % 60) + " sec");
                else System.out.println(duration + " sec");
            }

            double avg_lpp = 0, avg_lpp_t = 0, avg_spp = 0, avg_spp_t = 0;
            boolean errorLpp = false, errorSpp = false;

            for (int j = 0; j < N; j++) {
                System.out.println(i + " / " + iMax + ", " + j + " / " + N);
                DirectedGraph graph = generator.generate();
                LongestPathProblem lpp = new LongestPathProblem(graph);
                ShortestPathProblem spp = new ShortestPathProblem(graph);

                long t = System.currentTimeMillis();
                Path p;
                int ret = 0;
                do {
                    p = solver.solve(lpp);
                } while (p == null && (++ret < 5));
                t = System.currentTimeMillis() - t;

                if (p == null) {
                    errorLpp = true;
                } else {
                    avg_lpp += (double) p.getDistance() / N;
                    avg_lpp_t += (double) t / N;
                }

                t = System.currentTimeMillis();
                ret = 0;
                do {
                    p = solver.solve(spp);
                } while (p == null && (++ret < 5));
                t = System.currentTimeMillis() - t;

                if (p == null) {
                    errorSpp = true;
                } else {
                    avg_spp += (double) p.getDistance() / N;
                    avg_spp_t += (double) t / N;
                }
            }

            pw.println(i + "," + (errorLpp ? "nan,nan" : avg_lpp + "," + avg_lpp_t) + "," + (errorSpp ? "nan,nan" : avg_spp + "," + avg_spp_t));
            pw.flush();
        }

        pw.close();
        out.close();
    }


    private static void warmup() throws IOException {
        PathGenerator generator = new PathGenerator(8, 0.9, defaultDistribution);
        AntColonySolver solver = new AntColonySolver();

        for (int j = 0; j < 5; j++) {
            DirectedGraph graph = generator.generate();
            LongestPathProblem lpp = new LongestPathProblem(graph);
            ShortestPathProblem spp = new ShortestPathProblem(graph);

            solver.solve(lpp);
            solver.solve(spp);
        }
    }
}

