package g28.sos1.solvers;

public interface Solver<P, S> {
    S solve(P problem);
}
