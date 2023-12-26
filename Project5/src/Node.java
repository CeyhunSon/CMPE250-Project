import java.util.HashMap;

public class Node {
    public String name;
    public HashMap<String, Integer> graphList;
    public HashMap<String, Integer> rGraphList;

    public Node(String name) {
        this.name = name;
        graphList = new HashMap<>();
        rGraphList = new HashMap<>();
    }

    public void addAdjacentNode(Node node, int capacity) {
        graphList.put(node.name, capacity);
        rGraphList.put(node.name, capacity);
        if (node.rGraphList.get(name) == null) {
            node.graphList.put(name, 0);
            node.rGraphList.put(name, 0);
        }
    }
}
