import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class BST {

    protected Node root;
    private final FileWriter bstWriter;

    public BST(String rootIP, FileWriter bstWriter) {
        root = new Node(rootIP, 0, null, null, null);
        this.bstWriter = bstWriter;
    }

    public Node findMin(Node node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    /*public String findAndDeleteMin(Node node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        node.getParent().leftChild = node.rightChild;
        if (node.rightChild != null) {
            node.rightChild.setParent(node.getParent());
        }
        // node.setParent(null);
        return node.getIp();
    }*/

    public void addNode(String ip) throws IOException {
        root = addNode(root, ip);
    }

    public Node addNode(Node node, String ip) throws IOException {
        if (node == null) {return new Node(ip, 0, null, null, null);}
        else if (ip.compareTo(node.getIp()) < 0) {
            bstWriter.write(node.getIp() + ": New node being added with IP:" + ip + "\n");
            node.leftChild = addNode(node.leftChild, ip);
            node.leftChild.setParent(node);
        } else if (ip.compareTo(node.getIp()) > 0) {
            bstWriter.write(node.getIp() + ": New node being added with IP:" + ip + "\n");
            node.rightChild = addNode(node.rightChild, ip);
            node.rightChild.setParent(node);
        }
        return node;
    }

    /*public void delete(String ip) throws IOException {
        if (ip.equals(root.getIp())) {
            return;
        }
        delete(root, ip);
    }

    public Node delete(Node node, String ip) throws IOException {
        if (node == null) {
            return null;
        }
        if (ip.equals("73680")) {
            System.out.println(node.getIp());
        }
        if (ip.compareTo(node.getIp()) < 0) {
            node.leftChild = delete(node.leftChild, ip);
        } else if (ip.compareTo(node.getIp()) > 0) {
            node.rightChild = delete(node.rightChild, ip);
        } else {
            if (node.leftChild == null && node.rightChild == null) {
                bstWriter.write(node.getParent().getIp() + ": Leaf Node Deleted: " + ip + "\n");
                // node.setParent(null);
                return null;
            } else if (node.rightChild == null) {
                bstWriter.write(node.getParent().getIp() + ": Node with single child Deleted: " + ip + "\n");
                node.leftChild.setParent(node.getParent());
                // node.setParent(null);
                return node.leftChild;
            } else if (node.leftChild == null) {
                bstWriter.write(node.getParent().getIp() + ": Node with single child Deleted: " + ip + "\n");
                node.rightChild.setParent(node.getParent());
                // node.setParent(null);
                return node.rightChild;
            }
            Node replacingNode = findMin(node.rightChild);
            node.setIp(replacingNode.getIp());
            bstWriter.write(node.getParent().getIp() + ": Non Leaf Node Deleted; removed: " + ip + " replaced: " + node.getIp() + "\n");
            replacingNode.getParent().leftChild = replacingNode.rightChild;
            if (replacingNode.rightChild != null) {
                replacingNode.rightChild.setParent(replacingNode.getParent());
            }
        }
        return node;
    }*/

    public void delete(String ip) throws IOException {
        if (ip.equals(root.getIp())) {
            return;
        }
        delete(root, ip, false);
    }

    public Node delete(Node node, String ip, boolean isReplacing) throws IOException {
        /*if (node == null) {
            return null;
        }*/
        if (ip.compareTo(node.getIp()) < 0) {
            node.leftChild = delete(node.leftChild, ip, isReplacing);
        } else if (ip.compareTo(node.getIp()) > 0) {
            node.rightChild = delete(node.rightChild, ip, isReplacing);
        } else {
            if (node.leftChild == null && node.rightChild == null) {
                if (!isReplacing) {
                    bstWriter.write(node.getParent().getIp() + ": Leaf Node Deleted: " + ip + "\n");
                }
                // node.setParent(null);
                return null;
            } else if (node.rightChild == null) {
                if(!isReplacing) {
                    bstWriter.write(node.getParent().getIp() + ": Node with single child Deleted: " + ip + "\n");
                }
                node.leftChild.setParent(node.getParent());
                // node.setParent(null);
                return node.leftChild;
            } else if (node.leftChild == null) {
                if(!isReplacing) {
                    bstWriter.write(node.getParent().getIp() + ": Node with single child Deleted: " + ip + "\n");
                }
                node.rightChild.setParent(node.getParent());
                // node.setParent(null);
                return node.rightChild;
            }
            node.setIp(findMin(node.rightChild).getIp());
            bstWriter.write(node.getParent().getIp() + ": Non Leaf Node Deleted; removed: " + ip + " replaced: " + node.getIp() + "\n");
            node.rightChild = delete(node.rightChild, node.getIp(), true);
        }
        return node;
    }

    public void send(String senderIP, String receiverIP) throws IOException {
        LinkedList<Node> path = new LinkedList<>();
        Node node1 = root;
        Node node2 = root;
        boolean isCommonNodeFound = false;
        while (node1 != null) {
            if (!isCommonNodeFound) {
                if (senderIP.compareTo(node1.getIp()) < 0 && receiverIP.compareTo(node1.getIp()) < 0) {
                    node1 = node1.leftChild;
                } else if (senderIP.compareTo(node1.getIp()) > 0 && receiverIP.compareTo(node1.getIp()) > 0) {
                    node1 = node1.rightChild;
                } else {
                    isCommonNodeFound = true;
                    node2 = node1;
                    path.add(node1);
                }
            } else {
                if (senderIP.compareTo(node1.getIp()) < 0 && receiverIP.compareTo(node2.getIp()) < 0) {
                    node1 = node1.leftChild;
                    path.addFirst(node1);
                    node2 = node2.leftChild;
                    path.addLast(node2);
                } else if (senderIP.compareTo(node1.getIp()) > 0 && receiverIP.compareTo(node2.getIp()) > 0) {
                    node1 = node1.rightChild;
                    path.addFirst(node1);
                    node2 = node2.rightChild;
                    path.addLast(node2);
                } else if (senderIP.compareTo(node1.getIp()) < 0 && receiverIP.compareTo(node2.getIp()) > 0) {
                    node1 = node1.leftChild;
                    path.addFirst(node1);
                    node2 = node2.rightChild;
                    path.addLast(node2);
                } else if (senderIP.compareTo(node1.getIp()) > 0 && receiverIP.compareTo(node2.getIp()) < 0) {
                    node1 = node1.rightChild;
                    path.addFirst(node1);
                    node2 = node2.leftChild;
                    path.addLast(node2);
                } else if (senderIP.compareTo(node1.getIp()) == 0 && receiverIP.compareTo(node2.getIp()) > 0) {
                    node2 = node2.rightChild;
                    path.addLast(node2);
                } else if (senderIP.compareTo(node1.getIp()) == 0 && receiverIP.compareTo(node2.getIp()) < 0) {
                    node2 = node2.leftChild;
                    path.addLast(node2);
                } else if (senderIP.compareTo(node1.getIp()) < 0 && receiverIP.compareTo(node2.getIp()) == 0) {
                    node1 = node1.leftChild;
                    path.addFirst(node1);
                } else if (senderIP.compareTo(node1.getIp()) > 0 && receiverIP.compareTo(node2.getIp()) == 0) {
                    node1 = node1.rightChild;
                    path.addFirst(node1);
                } else {
                    break;
                }
            }
        }
        bstWriter.write(senderIP + ": Sending message to: " + receiverIP + "\n");
        Node previousNode = path.removeFirst();
        Node currentNode;
        while (path.size() != 1) {
            currentNode = path.removeFirst();
            bstWriter.write(currentNode.getIp() + ": Transmission from: " + previousNode.getIp() + " receiver: " + receiverIP + " sender:" + senderIP + "\n");
            previousNode = currentNode;
        }
        bstWriter.write(receiverIP + ": Received message from: " + senderIP + "\n");
    }
}
