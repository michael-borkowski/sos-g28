package g28.sos1.problems.graphs;

import java.util.Set;

public class Node {
    private final String name;
    private DirectedGraph parent = null;

    public Node(DirectedGraph parent, String name) {
        if(parent == null || name == null) throw new NullPointerException(("parent: " + parent + ", name: " + name));

        this.parent = parent;
        this.name = name;
    }

    public void setParent(DirectedGraph parent) {
        if(this.parent != null) throw new IllegalStateException("parent of " + this + " already set");
        this.parent = parent;
    }

    public DirectedGraph getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public Set<Edge> getOutgoingEdges() {
        return parent.getOutgoingEdges(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Node) && ((Node) obj).name.equals(name);
    }
}
