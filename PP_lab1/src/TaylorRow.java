import java.util.Scanner;
import java.util.InputMismatchException;

class Parameters {
    public int k;
    public double x;

    Parameters(int k, double x) {
        this.k = k;
        this.x = x;
    }

    Parameters() {
        this.k = 0;
        this.x = 0;
    }

}

public class TaylorRow {

    public static void main(String[] args) {
        System.out.println("This program calculates function sqrt(1+x) using Taylor row." +
                "The calculations are completed when the next term turns out to be modulo less then 10^(-k)");

        Scanner in = new Scanner(System.in);
        Parameters obj = new Parameters();
        while (true) {
            try {
                enterNumbers(obj, in);
                break;
            } catch (InputMismatchException ex) {
                System.out.println("Error: Invalid data format");
                in.nextLine(); //
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
                in.nextLine();
            }
        }

        System.out.printf("Taylor row result: %.6f\n", calculateRow(obj.k, obj.x));
        System.out.printf("Standart functions result: %.6f\n", calculateFunc(obj.x));

    }

    static void enterNumbers(Parameters obj, Scanner in)  {
        System.out.println("Enter natural k and real х ∈ (-1, 1) through a space");
        obj.k = in.nextInt();
        obj.x = in.nextDouble();
        if (obj.k < 1 || obj.x > 1 || obj.x < -1) {
            throw new IllegalArgumentException("The range of numbers is broken");
        }

    }

    static double calculateRow(int k, double x) {
        double epsilon = Math.pow(10, -k), sum = 1.0, term = 1.0, multiplier;
        final double alpha = 0.5;
        int n = 1;

        while (true) {
            multiplier = (alpha - (n - 1)) / n;
            term = term * multiplier * x;

            if (Math.abs(term) < epsilon) {
                break;
            }
            sum += term;
            n++;
        }
        return sum;
    }

    static double calculateFunc(double x) {
         return Math.sqrt(x + 1);
    }
}
