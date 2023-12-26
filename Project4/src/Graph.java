import java.util.*;

public class Graph {
    public HashMap<String, Node> nodeMap;
    public HashSet<String> nodeNames;
    public ArrayList<Node> nodes;
    public ArrayList<Node> flags;
    public PriorityQueue<Node> pq;
    public PriorityQueue<Node> flagPQ;
    public Node startingNode;
    public Node endingNode;
    public Graph() {
        nodeMap = new HashMap<>();
        nodeNames = new HashSet<>();
        nodes = new ArrayList<>();
        flags = new ArrayList<>();
        pq = new PriorityQueue<>();
        flagPQ = new PriorityQueue<>();
    }

    public void createNode(String name, boolean isFlag) {
        if (nodeNames.contains(name)) {
            return;
        }
        Node node = new Node(name, isFlag);
        nodeNames.add(name);
        nodes.add(node);
        nodeMap.put(name, node);
        if (isFlag) {
            flags.add(node);
        }
    }

    public void adjustFirstAndLast(String startingName, String endingName) {
        startingNode = nodeMap.get(startingName);
        endingNode = nodeMap.get(endingName);
    }
    public void adjustNeighborhood (String theNodeName, String adjacentNodeName, int dist) {
        nodeMap.get(theNodeName).addNeighbor(nodeMap.get(adjacentNodeName), dist);
    }

    public void resetCosts() {
        for (Node node : nodes) {
            node.cost = Integer.MAX_VALUE;
        }
    }

    public int calculateShortestPath() {
        HashSet<Node> visitedNodes = new HashSet<>();
        startingNode.cost = 0;
        pq.add(startingNode);
        while (visitedNodes.size() != nodes.size()) {
            if (pq.isEmpty()) {
                break;
            }
            Node currentNode = pq.poll();
            if (visitedNodes.contains(currentNode)) {
                continue;
            }
            visitedNodes.add(currentNode);
            for (Map.Entry<Node, Integer> adjacentNode: currentNode.adjacentNodes.entrySet()) {
                if (visitedNodes.contains(adjacentNode.getKey())) {
                    continue;
                }
                if (currentNode.cost + adjacentNode.getValue() < adjacentNode.getKey().cost) {
                    adjacentNode.getKey().cost = currentNode.cost + adjacentNode.getValue();
                }
                pq.add(adjacentNode.getKey());
            }
            if (currentNode.equals(endingNode)) {
                int pathCost = endingNode.cost;
                resetCosts();
                return pathCost;
            }
        }
        resetCosts();
        return -1;
    }

    public int calculateFlagPath() {
        HashSet<Node> visitedFlags = new HashSet<>();
        HashSet<Node> visitedNodes = new HashSet<>();

        HashSet<String> nn = new HashSet<>();

        flags.get(0).cost = 0;
        flagPQ.add(flags.get(0));

        while (visitedNodes.size() != nodes.size()) {
            if (visitedFlags.size() == flags.size()) {
                int totalFlagCost = 0;
                for (Node flag : flags) {
                    totalFlagCost += flag.cost;
                }
                return totalFlagCost;
            }

            if(flagPQ.isEmpty()) {
                break;
            }
            Node currentNode = flagPQ.poll();

            if (nn.contains(currentNode.name)) {
                continue;
            }

            if (currentNode.isFlag) {
                nn.add(currentNode.name);
            }

            if (currentNode.isFlag) {
                visitedFlags.add(currentNode);
            }

            for (Map.Entry<Node, Integer> adjacentNode: currentNode.adjacentNodes.entrySet()) {
                if (visitedFlags.contains(adjacentNode.getKey())) continue;
                if (currentNode.isFlag) {
                    if (adjacentNode.getValue() < adjacentNode.getKey().cost) {
                        adjacentNode.getKey().parent = currentNode;
                        adjacentNode.getKey().cost = adjacentNode.getValue();
                        flagPQ.remove(adjacentNode.getKey());
                        flagPQ.add(adjacentNode.getKey());
                    }
                }else if (currentNode.cost + adjacentNode.getValue() < adjacentNode.getKey().cost) {
                    adjacentNode.getKey().cost = currentNode.cost + adjacentNode.getValue();
                    flagPQ.remove(adjacentNode.getKey());
                    flagPQ.add(adjacentNode.getKey());
                }
            }

        }
        return -1;
    }
}
