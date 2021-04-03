import java.util.ArrayList;

import java.util.PriorityQueue;
import java.util.Comparator;

public class Graph {
    private ArrayList<Node> nodes;
    public Graph() {
        this.nodes = new ArrayList<Node>();
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
    public void aStar(String from, String to) {
        if (find(from) == null && find(to) == null) return;
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
                return;
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

class TupleNodeCostComparator implements Comparator<TupleNodeCost> {
    @Override
    public int compare(TupleNodeCost tnc1, TupleNodeCost tnc2) {
        if (tnc1.getCost() > tnc2.getCost()) return 1;
        else if (tnc1.getCost() < tnc2.getCost()) return -1;
        return 0;
    }
}