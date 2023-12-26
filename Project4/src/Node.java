import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Node implements Comparable<Node>{
    public String name;
    public HashMap<Node, Integer> adjacentNodes;
    public LinkedList<Node> path;
    public Node parent;
    public int cost;
    public boolean isFlag;

    public Node(String name, boolean isFlag) {
        path = new LinkedList<>();
        adjacentNodes = new HashMap<>();
        this.name = name;
        this.cost = Integer.MAX_VALUE;
        this.isFlag = isFlag;
        parent = null;
        if (isFlag) {
            parent = this;
        }
    }

    public void addNeighbor(Node node, int distance) {
        adjacentNodes.put(node, distance);
        node.adjacentNodes.put(this, distance);
    }

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.cost, node.cost);
    }

    @Override
    public String toString() {
        return name;
    }
}
