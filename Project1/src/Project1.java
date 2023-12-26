import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Project1 {
    public static void main(String[] args) {
        FactoryImpl factory = new FactoryImpl();
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
            Product product;
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] elements = line.split(" ");
                switch(elements[0]) {
                    case "AF":
                        product = new Product(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
                        factory.addFirst(product);
                        break;
                    case "AL":
                        product = new Product(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
                        factory.addLast(product);
                        break;
                    case "A":
                        try {
                            product = new Product(Integer.parseInt(elements[2]), Integer.parseInt(elements[3]));
                            factory.add(Integer.parseInt(elements[1]), product);
                        }catch(IndexOutOfBoundsException ea) {
                            System.out.println("Index out of bounds.");
                        }
                        break;
                    case "RF":
                        try {
                            System.out.println(factory.removeFirst());
                        }catch(NoSuchElementException erf) {
                            System.out.println("Factory is empty.");
                        }
                        break;
                    case "RL":
                        try {
                            System.out.println(factory.removeLast());
                        }catch(NoSuchElementException erl) {
                            System.out.println("Factory is empty.");
                        }
                        break;
                    case "RI":
                        try {
                            System.out.println(factory.removeIndex(Integer.parseInt(elements[1])));
                        }catch(IndexOutOfBoundsException eri) {
                            System.out.println("Index out of bounds.");
                        }
                        break;
                    case "RP":
                        try {
                            System.out.println(factory.removeProduct(Integer.parseInt(elements[1])));
                        }catch(NoSuchElementException erp) {
                            System.out.println("Product not found.");
                        }
                        break;
                    case "F":
                        try {
                            System.out.println(factory.find(Integer.parseInt(elements[1])));
                        }catch(NoSuchElementException ef) {
                            System.out.println("Product not found.");
                        }
                        break;
                    case "G":
                        try {
                            System.out.println(factory.get(Integer.parseInt(elements[1])));
                        }catch(IndexOutOfBoundsException eg) {
                            System.out.println("Index out of bounds.");
                        }
                        break;
                    case "U":
                        try {
                            System.out.println(factory.update(Integer.parseInt(elements[1]), Integer.parseInt(elements[2])));
                        }catch(NoSuchElementException eu) {
                            System.out.println("Product not found.");
                        }
                        break;
                    case "FD":
                        System.out.println(factory.filterDuplicates());
                        break;
                    case "R":
                        factory.reverse();
                        factory.print();
                        break;
                    case "P":
                        factory.print();
                        break;
                }
            }
            input.close();
        }catch(FileNotFoundException er) {
            er.printStackTrace();
        }
    }
}