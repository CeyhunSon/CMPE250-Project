import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            FileWriter bstWriter = new FileWriter(args[1] + "_bst.txt");
            FileWriter avlWriter = new FileWriter(args[1] + "_avl.txt");
            try {
                File inputFile = new File(args[0]);
                Scanner input = new Scanner(inputFile);
                String rootIP = input.nextLine();
                BST bst = new BST(rootIP, bstWriter);
                AVL avl = new AVL(rootIP, avlWriter);
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    String[] elements = line.split(" ");
                    switch (elements[0]) {
                        case "ADDNODE" -> {
                            bst.addNode(elements[1]);
                            avl.addNode(elements[1]);
                        }
                        case "DELETE" -> {
                            bst.delete(elements[1]);
                            avl.delete(elements[1]);
                        }
                        case "SEND" -> {
                            bst.send(elements[1], elements[2]);
                            avl.send(elements[1], elements[2]);
                        }
                    }
                }
                input.close();
            } catch(FileNotFoundException er) {
                er.printStackTrace();
            }
            bstWriter.close();
            avlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}