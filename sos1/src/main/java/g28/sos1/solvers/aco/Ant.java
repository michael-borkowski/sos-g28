package g28.sos1.solvers.aco;

import g28.sos1.problems.graphs.Edge;
import g28.sos1.problems.graphs.Node;

import java.util.LinkedList;
import java.util.List;

public class Ant {
    private Node position;
    private List<Edge> history = new LinkedList<>();
    private boolean alive = true;

    public Ant(Node position) {
        this.position = position;
    }

    public List<Edge> getHistory() {
        return history;
    }

    public void move(Edge e) {
        history.add(e);
        position = e.getB();
    }

    public Node getPosition() {
        return position;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }
}
