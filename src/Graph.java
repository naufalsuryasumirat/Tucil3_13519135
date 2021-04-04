import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class Graph {
    private ArrayList<Node> nodes;
    public Graph() {
        this.nodes = new ArrayList<Node>();
    }
    public Graph(ArrayList<Node> nodeList) {
        this.nodes = nodeList;
    }
    public void addNode(Node toAdd) {
        if (nodes.contains(toAdd)) return;
        this.nodes.add(toAdd);
    }
    public ArrayList<Node> getNodes() { return this.nodes; }
    public void printInfo() {
        for (Node node : nodes) {
            node.printInfo();
        }
    }
    public void printInfo(boolean check) {
        if (check) return;
        for (int i = 0; i < nodes.size(); i++) {
            System.out.print(nodes.get(i).getName());
            if (i != nodes.size() - 1) { System.out.print(", "); }
            else { System.out.println("."); }
        }
    }
    public Node find(String name) {
        for (Node node : nodes) {
            if (node.getName().equals(name)) return node;
        }
        return null;
    }
    public void reset() {
        for (Node node : nodes) {
            node.setFromNode(null);
        }
    }
    public ArrayList<String> aStar(String from, String to) {
        if (find(from) == null || find(to) == null || (from == to)) return null;
        PriorityQueue<TupleNodeCost> PQueue = 
            new PriorityQueue<TupleNodeCost>(new TupleNodeCostComparator());
        Node GoalNode = find(to);
        PQueue.add(new TupleNodeCost(find(from), 0.0, 0.0));
        while (!PQueue.isEmpty()) {
            TupleNodeCost Head = PQueue.poll();
            // System.out.println(Head.getCost()); // TEST
            if (Head.getNode().getName() == to) {
                System.out.println(Head.getCost()); // TEST
                System.out.println("GotPath"); // TEST
                Node path = Head.getNode();
                PQueue.clear();
                ArrayList<String> pathSteps = new ArrayList<String>();
                while (path != null) {
                    // System.out.println(path.getName()); // TEST
                    pathSteps.add(path.getName());
                    path = path.getFromNode();
                }
                for (int i = pathSteps.size() - 1; i >= 0; i--) {
                    System.out.print(pathSteps.get(i));
                    if (i != 0) { System.out.print(" --> "); }
                    else { System.out.println(); }
                }
                reset();
                List<String> listpath = pathSteps;
                Collections.reverse(listpath);
                return new ArrayList<String>(listpath);
            }
            for (TupleNode tupleNode : Head.getNode().getConnectedNodes()) {
                if (tupleNode.getNode().getName() == Head.getNode().getName()) continue;
                if (tupleNode.getNode().getName() == from) continue;
                if (tupleNode.getNode().getFromNode() != null &&
                    tupleNode.getNode().getName() == Head.getNode().getFromNode().getName()) continue;
                tupleNode.getNode().setFromNode(Head.getNode());
                PQueue.add(new TupleNodeCost(tupleNode.getNode(), 
                    Head.getDistanceSoFar() + tupleNode.getDistance(),
                    Head.getDistanceSoFar() + tupleNode.getDistance()
                    + tupleNode.getNode().getCoordinate()
                        .HaversineEuclideanDistance(GoalNode.getCoordinate())));
            }
        }
        return null; // return null jika tidak terdapat path
    }
    public TuplePathCost aStar(String from, String to, Boolean bool) {
        if (find(from) == null || find(to) == null) return null;
        PriorityQueue<TuplePathCost> PQueue = 
            new PriorityQueue<TuplePathCost>(new TuplePathCostComparator());
        Node GoalNode = find(to);
        Node InitialNode = find(from);
        PQueue.add(new TuplePathCost(InitialNode));
        while (!PQueue.isEmpty()) {
            TuplePathCost Head = PQueue.poll();
            if (Head.getLastNode().getName() == to) {
                for (TuplePathCost tpCost : PQueue) { // TEST
                    tpCost.printInfo();
                }
                System.out.println("INI SEKARANG PAKE POLL"); // TEST
                while (!PQueue.isEmpty()) { // TEST
                    PQueue.poll().printInfo();
                }
                System.out.println("\n\n"); // TEST
                PQueue.clear();
                reset();
                return Head;
            }
            else {
                for (var tpNode : Head.getLastNodeNeighbors()) {
                    if (Head.pathContains(tpNode.getNode())) continue;
                    tpNode.getNode().setFromNode(Head.getLastNode());
                    ArrayList<Node> toAdd = new ArrayList<Node>(Head.getList());
                    toAdd.add(tpNode.getNode());
                    PQueue.add(new TuplePathCost(toAdd,
                        Head.getDistanceSoFar() + tpNode.getDistance(), 
                        Head.getDistanceSoFar() + tpNode.getDistance()
                        + tpNode.getNode().getCoordinate()
                            .HaversineEuclideanDistance(GoalNode.getCoordinate())));
                }
            }
        }
        return null;
    }
}

class TupleNodeCost {
    private Node node;
    private Double distanceSoFar;
    private Double cost;
    public TupleNodeCost(Node node, Double distanceSoFar, Double cost) {
        this.node = node;
        this.distanceSoFar = distanceSoFar;
        this.cost = cost;
    }
    public Node getNode() { return this.node; }
    public Double getCost() { return this.cost; }
    public Double getDistanceSoFar() { return this.distanceSoFar; }
    public void printInfo() {
        System.out.print("(" + this.node.getName() + "," + this.cost + ")");
    }
}

class TuplePathCost {
    private ArrayList<Node> path;
    private Double distanceSoFar;
    private Double cost;
    public TuplePathCost(ArrayList<Node> path, Double distanceSoFar, Double cost) {
        this.path = path;
        this.distanceSoFar = distanceSoFar;
        this.cost = cost;
    }
    public TuplePathCost(Node node) {
        this.path = new ArrayList<Node>();
        this.path.add(node);
        this.distanceSoFar = 0.0;
        this.cost = 0.0;
    }
    public ArrayList<Node> getList() { return this.path; }
    public Node getLastNode() {
        if (this.path == null || this.path.size() == 0) return null;
        return this.path.get(path.size() - 1);
    }
    public Double getCost() { return this.cost; }
    public Double getDistanceSoFar() { return this.distanceSoFar; }
    public void addPath(Node node) { // TEST mungkin gaperlu
        this.path.add(node);
    }
    public void addCost(Double cost) { // TEST mungkin gaperlu
        this.cost += cost;
    }
    public void printInfo() {
        if (this.path == null || this.path.size() == 0) return;
        System.out.print("Path\t: ");
        for (int i = 0; i < this.path.size(); i++) {
            System.out.print(this.path.get(i).getName());
            if (i != this.path.size() - 1) { System.out.print(" --> "); }
            else { System.out.println("."); }
        }
        System.out.println("Distance: " + this.distanceSoFar.toString());
        System.out.println("Cost\t: " + this.cost.toString());
    }
    public boolean pathContains(Node node) {
        return this.path.contains(node);
    }
    // public PriorityQueue<TupleNode> getLastNodeNeighbors() { // TEST
    //     return getLastNode().getConnectedNodes();
    // }
    public ArrayList<TupleNode> getLastNodeNeighbors() {
        return getLastNode().getConnectedNodes();
    }
    public ArrayList<String> getPath() {
        if (this.path == null || this.path.size() == 0) return null;
        ArrayList<String> toReturn = new ArrayList<String>();
        for (Node node : path) {
            toReturn.add(node.getName());
        }
        return toReturn;
    }
}

class TupleNodeCostComparator implements Comparator<TupleNodeCost> {
    @Override
    public int compare(TupleNodeCost tnc1, TupleNodeCost tnc2) {
        if (tnc1.getCost() > tnc2.getCost()) return 1;
        else if (tnc1.getCost() < tnc2.getCost()) return -1;
        return 0;
    }
}

class TuplePathCostComparator implements Comparator <TuplePathCost> {
    @Override
    public int compare(TuplePathCost tpc1, TuplePathCost tpc2) {
        // if (tpc1.getCost() > tpc2.getCost()) return 1;
        if (tpc1.getCost().compareTo(tpc2.getCost()) < 0) return -1;
        // else if (tpc1.getCost() < tpc2.getCost()) return - 1;
        else if (tpc1.getCost().compareTo(tpc2.getCost()) > 0) return 1;
        else return 0;
    }
}