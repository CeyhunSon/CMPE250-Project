import java.util.*;

public class Graph {
    public int nodeNumber;
    public HashMap<String, Node> nodes;
    public HashSet<String> createdNodes;
    public Node startingNode;
    public Node kingsLanding;

    public Graph(int cityNumber) {
        nodeNumber = cityNumber + 8;
        nodes = new HashMap<>();
        createdNodes = new HashSet<>();
        startingNode = new Node("s");
        kingsLanding = new Node("KL");
        nodes.put("s", startingNode);
        nodes.put("KL", kingsLanding);
        createdNodes.add("s");
        createdNodes.add("KL");
    }

    public void createNode(String name) {
        if (createdNodes.contains(name))
            return;
        Node node = new Node(name);
        nodes.put(name, node);
        createdNodes.add(name);
    }

    public void adjustAdjacency(String startingNodeName, String endingNodeName, int capacity) {
        nodes.get(startingNodeName).addAdjacentNode(nodes.get(endingNodeName), capacity);
    }

    public boolean isThereAPath(HashMap<String, Node> nodeParent) {
        HashSet<String> visitedNodes = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(startingNode);
        visitedNodes.add("s");
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            for (Map.Entry<String, Integer> entry : node.rGraphList.entrySet()) {
                if (!visitedNodes.contains(entry.getKey()) && entry.getValue() > 0) {
                    nodeParent.put(nodes.get(entry.getKey()).name, node);
                    if (entry.getKey().equals("KL")) {
                        return true;
                    }
                    queue.add(nodes.get(entry.getKey()));
                    visitedNodes.add(entry.getKey());
                }
            }
        }
        return false;
    }

    public void setReachableNodes(Node node, HashSet<String> reachableNodes) {
        reachableNodes.add(node.name);
        Queue<Node> reachableQueue = new LinkedList<>();
        reachableQueue.add(node);
        while (!reachableQueue.isEmpty()) {
            node = reachableQueue.poll();
            for (Map.Entry<String, Integer> entry : node.rGraphList.entrySet()) {
                if (!reachableNodes.contains(entry.getKey()) && entry.getValue() > 0) {
                    reachableQueue.add(nodes.get(entry.getKey()));
                    reachableNodes.add(entry.getKey());
                }
            }
        }
    }

    public void printOutput() {
        HashMap<String, Node> nodeParent = new HashMap<>();
        int maxTroops = 0;
        while (isThereAPath(nodeParent)) {
            int pathCapacity = Integer.MAX_VALUE;
            for (Node node = kingsLanding; !node.name.equals("s"); node = nodeParent.get(node.name)) {
                pathCapacity = Math.min(nodeParent.get(node.name).rGraphList.get(node.name), pathCapacity);
            }
            for (Node node = kingsLanding; !node.name.equals("s"); node = nodeParent.get(node.name)) {
                int newValParent = nodeParent.get(node.name).rGraphList.get(node.name) - pathCapacity;
                int newValChild = node.rGraphList.get(nodeParent.get(node.name).name) + pathCapacity;
                nodeParent.get(node.name).rGraphList.put(node.name, newValParent);
                node.rGraphList.put(nodeParent.get(node.name).name, newValChild);
            }
            maxTroops += pathCapacity;
        }
        System.out.println(maxTroops);
        HashSet<String> reachableNodes = new HashSet<>();
        setReachableNodes(startingNode, reachableNodes);

        for (Map.Entry<String, Node> entry1 : nodes.entrySet()) {
            for (Map.Entry<String, Integer> entry2 : entry1.getValue().graphList.entrySet()) {
                if (reachableNodes.contains(entry1.getKey()) && !reachableNodes.contains(entry2.getKey()) && entry2.getValue() > 0) {
                    if (entry1.getKey().equals("s")) {
                        System.out.println(entry2.getKey());
                    } else {
                        System.out.println(entry1.getKey() + " " + entry2.getKey());
                    }
                }
            }
        }
    }
}
