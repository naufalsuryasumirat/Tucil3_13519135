import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.ArrayList;

public class FileHandler {
    private ArrayList<Node> nodeList;
    private String filePath;
    private String fileName;
    public FileHandler() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter extensionType = 
            new FileNameExtensionFilter("txt", "txt");
        fileChooser.setFileFilter(extensionType);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue != JFileChooser.APPROVE_OPTION) return;
        this.filePath = fileChooser.getSelectedFile().getAbsolutePath();
        this.fileName = fileChooser.getSelectedFile().getName();
        try {
            this.nodeList = new ArrayList<Node>();
            File toRead = new File(this.filePath);
            Scanner readScan = new Scanner(toRead);
            String first = readScan.nextLine();
            Integer count = Integer.parseInt(first.toString());
            for (int i = 0; i < count; i++) {
                String line = readScan.nextLine();
                String array[] = line.split(" ");
                this.nodeList.add(new Node(array[0], 
                    new Coordinate(Double.valueOf(array[1]), 
                        Double.valueOf(array[2]))));
            }
            for (int i = 0; i < count; i++) {
                String line = readScan.nextLine();
                String array[] = line.split(" ");
                for (int j = 0; j < array.length; j++) {
                    if (Double.valueOf(array[j]) == 0.0) continue;
                    else this.nodeList.get(i).addConnection(this.nodeList.get(j));
                }
            }
            readScan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error, File not found!");
            e.printStackTrace();
        }
    }
    public Graph toGraph() {
        Graph g = new Graph(this.nodeList);
        return g;
    }
    public void printInfo() {
        System.out.println("Path\t: " + this.filePath);
        System.out.println("Name\t: " + this.fileName);
    }
}
