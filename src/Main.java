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
        graph.aStar("kwitang", "salemboi");
        graph.aStar("kwitang", "ancol");
        graph.aStar("kwitang", "pik");
        graph.aStar("pik", "tebet");
    }
}
