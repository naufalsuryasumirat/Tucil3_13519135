import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.Math;

public class Node {
    private String name;
    private PriorityQueue<TupleNode> cNodes;
    private Node fromNode;
    private Coordinate coordinate;
    public Node(String name, Coordinate coordinate) {
        this.cNodes = new PriorityQueue<TupleNode>(new TupleNodeComparator());
        this.name = name;
        this.fromNode = null;
        this.coordinate = coordinate;
    }
    public String getName() {
        return this.name;
    }
    public Node getFromNode() {
        return this.fromNode;
    }
    public Coordinate getCoordinate() {
        return this.coordinate;
    }
    public void setFromNode(Node from) {
        this.fromNode = from;
    }
    public void addConnection(Node node) {
        if (getName() == node.getName()) return; // Guard jika menambah koneksi ke diri sendiri
        for (TupleNode tupleNode : cNodes) { // Guard jika sudah connected
            if (tupleNode.getNode().getName() == node.getName()) return;
        }
        Double distance = getCoordinate().HaversineEuclideanDistance(node.getCoordinate());
        node.cNodes.add(new TupleNode(this, distance));
        this.cNodes.add(new TupleNode(node, distance));
    }
    public void addConnection(Node node, Double distance) {
        if (getName() == node.getName()) return; // Guard jika menambah koneksi ke diri sendiri
        for (TupleNode tupleNode : cNodes) { // Guard jika node sudah connected
            if (tupleNode.getNode().getName() == node.getName()) return;
        }
        node.cNodes.add(new TupleNode(this, distance));
        this.cNodes.add(new TupleNode(node, distance));
    }
    public PriorityQueue<TupleNode> getConnectedNodes() {
        return this.cNodes;
    }
    public void printInfo() {
        System.out.println("Name\t\t: " + getName());
        System.out.print("FNode\t\t: ");
        if (fromNode != null) System.out.println(fromNode.getName());
        else System.out.println("null");
        System.out.print("Coordinate\t: "); getCoordinate().printInfo(); System.out.println();
        System.out.print("CNodes\t\t: ");
        for (TupleNode tupleNode : cNodes) {
            tupleNode.printInfo(); System.out.print("; ");
        }
        System.out.println("\n");
    }
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return (node.getName().compareTo(getName()) == 0);
    }
}

class TupleNode {
    private Node node;
    private Double distance;
    public TupleNode(Node node, Double distance) {
        this.node = node;
        this.distance = distance;
    }
    public Node getNode() {
        return this.node;
    }
    public Double getDistance() {
        return this.distance;
    }
    public void printInfo() {
        System.out.print("(" + getNode().getName() + ", " + getDistance() + ")");
    }
}

class Coordinate {
    private Double latitude;
    private Double longitude;
    public Coordinate() {
        this.latitude = 0.0;
        this.longitude = 0.0;
    }
    public Coordinate(Double x, Double y) {
        this.latitude = x;
        this.longitude = y;
    }
    public Double getLatitude() {
        return this.latitude;
    }
    public Double getLongitude() {
        return this.longitude;
    }
    public Double HaversineEuclideanDistance(Coordinate other) {
        // Referensi: https://moveable-type.co.uk/scripts/latlong.html
        Double radius = 6371000.0; // Radius Bumi
        Double radLat1 = Math.toRadians(getLatitude());
        Double radLat2 = Math.toRadians(other.getLatitude());
        Double deltaLatitude = Math.toRadians(other.getLatitude() - getLatitude());
        Double deltaLongitude = Math.toRadians(other.getLongitude() - getLongitude());
        Double a = Math.pow(Math.sin(deltaLatitude / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(deltaLongitude / 2), 2);
        Double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = radius * b * 0.001;
        return Math.round(distance * 100.0) / 100.0;
    }
    public void printInfo() {
        System.out.print("(" + getLatitude() + ", " + getLongitude() + ")");
    }
}

class TupleNodeComparator implements Comparator<TupleNode> {
    @Override
    public int compare(TupleNode tn1, TupleNode tn2) {
        if (tn1.getDistance() > tn2.getDistance()) return 1;
        else if (tn1.getDistance() < tn2.getDistance()) return -1;
        return 0;
    }
}