package org.example;

import java.util.Scanner;
import java.util.Set;

public class GraphDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int count;

        while (true) {
            System.out.println("Enter the number of edges:");

            count = in.nextInt();
            if (count >= 1) {
                break;
            } else {
                System.out.println("Error: Number of count must be at least 1");
                in.nextLine();
            }
        }

        Graph graph = new Graph(count);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add edge");
            System.out.println("2. Remove edge");
            System.out.println("3. Check edge");
            System.out.println("4. Show graph");
            System.out.println("5. Get neighbors");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    handleAddEdge(in, graph);
                    break;

                case 2:
                    handleRemoveEdge(in, graph);
                    break;

                case 3:
                    handleCheckEdge(in, graph);
                    break;

                case 4:
                    System.out.println(graph);
                    break;

                case 5:
                    handleGetNeighbors(in, graph);
                    break;

                case 6:
                    System.out.println("Goodbye!");
                    in.close();
                    return;

                default:
                    System.out.println("Invalid option. Please choose 1-6");
            }
        }
    }

    private static void handleAddEdge(Scanner in, Graph graph) {
        System.out.print("Enter vertex 1: ");
        int v1 = in.nextInt();
        System.out.print("Enter vertex 2: ");
        int v2 = in.nextInt();

        try {
            graph.addEdge(v1, v2);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleRemoveEdge(Scanner in, Graph graph) {
        System.out.print("Enter vertex 1: ");
        int v1 = in.nextInt();
        System.out.print("Enter vertex 2: ");
        int v2 = in.nextInt();

        try {
            graph.removeEdge(v1, v2);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleCheckEdge(Scanner in, Graph graph) {
        System.out.print("Enter vertex 1: ");
        int v1 = in.nextInt();
        System.out.print("Enter vertex 2: ");
        int v2 = in.nextInt();

        try {
            boolean hasEdge = graph.hasEdge(v1, v2);
            System.out.println("Edge " + v1 + "-" + v2 + " exists: " + hasEdge);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleGetNeighbors(Scanner in, Graph graph) {
        System.out.print("Enter vertex: ");
        int vertex = in.nextInt();
        try {
            Set<Integer> neighbors = graph.getNeighbors(vertex);
            System.out.println("Neighbors of " + vertex + ": " + neighbors);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


