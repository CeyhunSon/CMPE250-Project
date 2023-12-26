import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class project5 {
    public static void main(String[] args) {
        PrintStream outputFile = null;
        try {
            outputFile = new PrintStream(args[1]);
        }catch(FileNotFoundException ew) {
            ew.printStackTrace();
        }
        System.setOut(outputFile);
        try {
            File inputFile = new File(args[0]);
            Scanner input = new Scanner(inputFile);
            int cityNumber = Integer.parseInt(input.nextLine());
            Graph graph = new Graph(cityNumber);
            String[] armyTroops = input.nextLine().split(" ");
            for (int i = 0; i < 6; i++) {
                String[] regionPaths = input.nextLine().split(" ");
                graph.createNode(regionPaths[0]);
                graph.adjustAdjacency("s", regionPaths[0], Integer.parseInt(armyTroops[i]));
                for (int j = 0; j < (regionPaths.length-1)/2; j++) {
                    graph.createNode(regionPaths[2*j+1]);
                    graph.adjustAdjacency(regionPaths[0], regionPaths[2*j+1], Integer.parseInt(regionPaths[2*j+2]));
                }
            }
            for (int k = 0; k < cityNumber; k++) {
                String[] cityPaths = input.nextLine().split(" ");
                graph.createNode(cityPaths[0]);
                for (int l = 0; l < (cityPaths.length-1)/2; l++) {
                    graph.createNode(cityPaths[2*l+1]);
                    graph.adjustAdjacency(cityPaths[0], cityPaths[2*l+1], Integer.parseInt(cityPaths[2*l+2]));
                }
            }
            graph.printOutput();

            input.close();
        }catch(FileNotFoundException er) {
            er.printStackTrace();
        }
    }
}