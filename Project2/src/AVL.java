import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class AVL {

    protected Node root;
    private final FileWriter avlWriter;

    public AVL(String rootIP, FileWriter avlWriter) {
        root = new Node(rootIP, 0, null, null, null);
        this.avlWriter = avlWriter;
    }

    public int getHeight(Node node) {
        if (node == null) {return -1;}
        return node.height;
    }

    public Node findMin(Node node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    public Node leftRotate(Node node) {
        Node newParent = node.rightChild;
        node.rightChild = newParent.leftChild;
        if (newParent.leftChild != null) {
            newParent.leftChild.setParent(node);
        }
        newParent.leftChild = node;
        newParent.setParent(node.getParent());
        node.setParent(newParent);
        if (node.equals(root)) {
            root = newParent;
        }
        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        newParent.height = Math.max(getHeight(newParent.leftChild), getHeight(newParent.rightChild)) + 1;
        return newParent;
    }

    public Node rightRotate(Node node) {
        Node newParent = node.leftChild;
        node.leftChild = newParent.rightChild;
        if (newParent.rightChild != null) {
            newParent.rightChild.setParent(node);
        }
        newParent.rightChild = node;
        newParent.setParent(node.getParent());
        node.setParent(newParent);
        if (node.equals(root)) {
            root = newParent;
        }
        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        newParent.height = Math.max(getHeight(newParent.leftChild), getHeight(newParent.rightChild)) + 1;
        return newParent;
    }

    public Node balance(Node node) throws IOException {
        if (getHeight(node.leftChild) - getHeight(node.rightChild) > 1) {
            if (getHeight(node.leftChild.leftChild) - getHeight(node.leftChild.rightChild) >= 0) {
                avlWriter.write("Rebalancing: right rotation" + "\n");
            } else {
                avlWriter.write("Rebalancing: left-right rotation" + "\n");
                node.leftChild = leftRotate(node.leftChild);
            }
            node = rightRotate(node);
        } else if (getHeight(node.leftChild) - getHeight(node.rightChild) < -1) {
            if (getHeight(node.rightChild.rightChild) - getHeight(node.rightChild.leftChild) >= 0) {
                avlWriter.write("Rebalancing: left rotation" + "\n");
            } else {
                avlWriter.write("Rebalancing: right-left rotation" + "\n");
                node.rightChild = rightRotate(node.rightChild);
            }
            node = leftRotate(node);
        }
        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        return node;
    }

    public void addNode(String ip) throws IOException {
        root = addNode(root, ip);
    }

    public Node addNode(Node node, String ip) throws IOException {
        if (node == null) {return new Node(ip, 0, null, null, null);}
        else if (ip.compareTo(node.getIp()) < 0) {
            avlWriter.write(node.getIp() + ": New node being added with IP:" + ip + "\n");
            node.leftChild = addNode(node.leftChild, ip);
            node.leftChild.setParent(node);
        } else if (ip.compareTo(node.getIp()) > 0) {
            avlWriter.write(node.getIp() + ": New node being added with IP:" + ip + "\n");
            node.rightChild = addNode(node.rightChild, ip);
            node.rightChild.setParent(node);
        }
        return balance(node);
    }

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
                    avlWriter.write(node.getParent().getIp() + ": Leaf Node Deleted: " + ip + "\n");
                }
                // node.setParent(null);
                return null;
            } else if (node.rightChild == null) {
                if(!isReplacing) {
                    avlWriter.write(node.getParent().getIp() + ": Node with single child Deleted: " + ip + "\n");
                }
                node.leftChild.setParent(node.getParent());
                // node.setParent(null);
                return node.leftChild;
            } else if (node.leftChild == null) {
                if(!isReplacing) {
                    avlWriter.write(node.getParent().getIp() + ": Node with single child Deleted: " + ip + "\n");
                }
                node.rightChild.setParent(node.getParent());
                // node.setParent(null);
                return node.rightChild;
            }
            node.setIp(findMin(node.rightChild).getIp());
            avlWriter.write(node.getParent().getIp() + ": Non Leaf Node Deleted; removed: " + ip + " replaced: " + node.getIp() + "\n");
            node.rightChild = delete(node.rightChild, node.getIp(), true);
        }
        return balance(node);
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
        avlWriter.write(senderIP + ": Sending message to: " + receiverIP + "\n");
        Node previousNode = path.removeFirst();
        Node currentNode;
        while (path.size() != 1) {
            currentNode = path.removeFirst();
            avlWriter.write(currentNode.getIp() + ": Transmission from: " + previousNode.getIp() + " receiver: " + receiverIP + " sender:" + senderIP + "\n");
            previousNode = currentNode;
        }
        avlWriter.write(receiverIP + ": Received message from: " + senderIP + "\n");
    }
}
