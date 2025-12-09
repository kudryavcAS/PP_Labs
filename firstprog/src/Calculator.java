import java.util.Scanner;
import java.util.InputMismatchException;

public class Calculator {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String text = """
                Выберите действие:
                0: Exit
                1: Addition
                2: Subtraction
                3: Multiplication
                4: Division
                5: Factorial
                """;
        System.out.println(text);
        int i = 0;
        try {
            i = in.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println("Error: the entered symbol does not meet the condition.");
            main(null);
        }

        switch (i) {
            case 0:
                return;
            case 1:
                printSum();
                main(null);
                break;
            case 2:
                printDif();
                main(null);
                break;
            case 3:
                printMul();
                main(null);
                break;
            case 4:
                printDiv();
                main(null);
                break;
            case 5:
                printFact();
                main(null);
                break;
            default:
                System.out.println("Operation not found");
                main(null);
        }
    }

    static void printSum() {
        double x, y;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the first term:");
        x = in.nextDouble();
        System.out.println("Enter the second term:");
        y = in.nextDouble();
        System.out.println("The sum:\n" + sum(x, y));
    }

    static double sum(double x, double y) {
        return x + y;
    }

    static void printDif() {
        double x, y;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a diminutive:");
        x = in.nextDouble();
        System.out.println("Enter the deductible:");
        y = in.nextDouble();
        System.out.println("The difference:\n" + dif(x, y));
    }

    static double dif(double x, double y) {
        return x - y;
    }

    static void printMul() {
        double x, y;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the first multiplier:");
        x = in.nextDouble();
        System.out.println("Enter the second multiplier:");
        y = in.nextDouble();
        System.out.println("The product:\n" + mul(x, y));
    }

    static double mul(double x, double y) {
        return x * y;
    }

    static void printDiv() {
        double x, y;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the divisible:");
        x = in.nextDouble();
        System.out.println("Enter the divisor:");
        y = in.nextDouble();
        try {
            System.out.println("The quotient:\n" + div(x, y));
        } catch (ArithmeticException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    static double div(double x, double y) {
        if (y == 0) {
            throw new ArithmeticException("Dividing into zero");
        }
        return x / y;
    }

    static void printFact() {
        int x;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter an integer X not exceeding 20:");
        x = in.nextInt();
        System.out.println("X! = " + fact(x));
    }

    static long fact(int x) {
        if (x == 1) {
            return 1;
        }
        return x * fact(x - 1);
    }
}
