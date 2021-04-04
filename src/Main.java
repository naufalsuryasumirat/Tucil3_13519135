import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, this is a test!");
        Node kwitang = new Node("kwitang", new Coordinate(-6.18, 106.83));
        Node ancol = new Node("ancol", new Coordinate(-6.12, 106.85));
        Node pik = new Node("pik", new Coordinate(-6.09, 106.74));
        Node salemboi = new Node("salemboi", new Coordinate(-6.19, 106.85));
        Node tebet = new Node("tebet", new Coordinate(-6.23, 106.85));
        
        // Adding Connections
        kwitang.addConnection(ancol);
        kwitang.addConnection(tebet);
        ancol.addConnection(pik);
        ancol.addConnection(salemboi);
        ancol.addConnection(tebet);
        pik.addConnection(salemboi);
        salemboi.addConnection(tebet);

        // Testing Guards
        tebet.addConnection(kwitang);
        tebet.addConnection(ancol);
        tebet.addConnection(tebet);
        ancol.addConnection(tebet);
        // Testing ends here

        // Prints infos
        kwitang.printInfo();
        ancol.printInfo();
        pik.printInfo();
        salemboi.printInfo();
        tebet.printInfo();

        // Graph class
        Graph graph = new Graph();
        graph.addNode(kwitang);
        graph.addNode(ancol);
        graph.addNode(pik);
        graph.addNode(salemboi);
        graph.addNode(tebet);

        // Testing Guard
        graph.addNode(tebet);
        graph.addNode(salemboi);
        // Testing ends here

        // Prints infos
        // graph.printInfo();
        graph.printInfo(false);
        var a1 = graph.aStar("kwitang", "salemboi");
        var a2 = graph.aStar("kwitang", "ancol");
        var a3 = graph.aStar("kwitang", "pik");
        var a4 = graph.aStar("pik", "tebet");

        // Print the return types
        System.out.println("\n\n");
        printInfo(a1);
        printInfo(a2);
        printInfo(a3);
        printInfo(a4);

        // Second test
        graph.printInfo(false);
        var b1 = graph.aStar("kwitang", "salemboi", false);
        var b2 = graph.aStar("kwitang", "ancol", false);
        var b3 = graph.aStar("kwitang", "pik", false);
        var b4 = graph.aStar("pik", "tebet", false);

        // Testing guards
        var b5 = graph.aStar("from", "to", false);
        var b6 = graph.aStar("kwitang", "kwitang", false);
        if (b5 == null) System.out.println("b5 null");

        // Printing results
        System.out.println("Results");
        b1.printInfo();
        b2.printInfo();
        b3.printInfo();
        b4.printInfo();
        b6.printInfo();

        // Reading from File
        // FileHandler F = new FileHandler();
        // Graph graphFile = F.toGraph();
        // graphFile.printInfo();
    }
    public static void printInfo(ArrayList<String> list) {
        if (list == null) { System.out.println("Tidak terdapat jalur"); return; }
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i != list.size() - 1) { System.out.print(" --> "); }
            else { System.out.println("."); }
        }
    }
}
