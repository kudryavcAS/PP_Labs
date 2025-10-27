package org.students;

import java.util.Scanner;

public class StudentOperationDemo {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("    Student Operations Demo");

        while (true) {
            showMenu();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    performOperation(Operations.INTERSECTION);
                    break;
                case 2:
                    performOperation(Operations.UNION);
                    break;
                case 3:
                    performOperation(Operations.DIFFERENCE);
                    break;
                case 0:
                    System.out.println("Program completed.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n  Operations Menu");
        System.out.println("1. " + Operations.INTERSECTION);
        System.out.println("2. " + Operations.UNION);
        System.out.println("3. " + Operations.DIFFERENCE);
        System.out.println("0. Exit");
        System.out.print("Choose operation: ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void performOperation(Operations operation) {
        System.out.print("Enter first file name: ");
        String file1 = scanner.nextLine();
        System.out.print("Enter second file name: ");
        String file2 = scanner.nextLine();
        System.out.print("Enter result file name: ");
        String resultFile = scanner.nextLine();

        StudentOperation studentOp = createOperation(operation);

        assert studentOp != null;
        studentOp.readFirstFiles(file1);
        studentOp.readSecondFiles(file2);

        System.out.println("Executing operation: " + operation);
        studentOp.operation();

        if (studentOp.getOperationResult() != null) {
            studentOp.printToFile(resultFile, studentOp.getOperationResult());
            System.out.println("Result saved to file: " + resultFile);
            System.out.println("Number of students in result: " + studentOp.getOperationResult().size());
        }
    }

    private static StudentOperation createOperation(Operations operation) {
        switch (operation) {
            case INTERSECTION:
                return new StudentIntersection();
            case UNION:
                return new StudentUnion();
            case DIFFERENCE:
                return new StudentDifference();
            default:
                return null;
        }
    }
}