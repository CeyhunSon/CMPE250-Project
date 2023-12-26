public class Node {

    private String ip;
    protected int height;
    protected Node leftChild;
    protected Node rightChild;
    private Node parent;

    public Node(String ip, int height, Node leftChild, Node rightChild, Node parent) {
        this.ip = ip;
        this.height = height;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
    }

    public String getIp() {return ip;}

    public void setIp(String ip) {this.ip = ip;}

    public Node getParent() {return parent;}

    public void setParent(Node parent) {this.parent = parent;}
}
