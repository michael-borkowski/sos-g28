package g28.sos1.problems.graphs;

public class Edge {
    private final Node a, b;
    private final long distance;

    public Edge(Node a, Node b, long distance) {
        if(a == null || b == null) throw new NullPointerException(("a: " + a + ", b: " + b));

        this.a = a;
        this.b = b;
        this.distance = distance;
    }

    public long getDistance() {
        return distance;
    }

    public Node getA() {
        return a;
    }

    public Node getB() {
        return b;
    }

    @Override
    public String toString() {
        return a + " -> " + b + " (" + distance + ")";
    }

    @Override
    public int hashCode() {
        return (int) (a.hashCode() + b.hashCode() +
                distance);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Edge)) return false;
        Edge other = (Edge)obj;
        return a.equals(other.a) && b.equals(other.b) && distance == other.distance;
    }
}
