import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class project4 {
    public static void main(String[] args) {
        Graph graph = new Graph();
        PrintStream outputFile = null;
        try {
            outputFile = new PrintStream(args[1]);
        }catch(FileNotFoundException ew) {
            ew.printStackTrace();
        }
        System.setOut(outputFile);
        try {
//            File inputFile = new File(args[0]);
            File inputFile = new File("C:/Users/csony/OneDrive/Documents/Project4/inp3.in");
            Scanner input = new Scanner(inputFile);
            int nodeNumber = Integer.parseInt(input.nextLine());
            int flagNumber = Integer.parseInt(input.nextLine());
            String[] line3 = input.nextLine().split(" ");
            String[] flagNames = input.nextLine().split(" ");
            for (String flagName : flagNames) {
                graph.createNode(flagName, true);
            }
            for (int i = 0; i < nodeNumber; i++) {
                String[] nodeLine = input.nextLine().split(" ");
                graph.createNode(nodeLine[0], false);
                for (int j = 0; j < (nodeLine.length-1)/2; j++) {
                    graph.createNode(nodeLine[2*j+1], false);
                    graph.adjustNeighborhood(nodeLine[0], nodeLine[2*j+1], Integer.parseInt(nodeLine[2*j+2]));
                }
            }
            graph.adjustFirstAndLast(line3[0], line3[1]);
            System.out.println(graph.calculateShortestPath());
            System.out.println(graph.calculateFlagPath());

            input.close();
        }catch(FileNotFoundException er) {
            er.printStackTrace();
        }
    }
}