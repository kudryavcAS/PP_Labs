package org.example;

import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

public class Matrix {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int rows = 0, cols = 0;
        try {
            System.out.println("Enter the number of rows of the matrix:");
            rows = in.nextInt();
            System.out.println("Enter the number of columns of the matrix:");
            cols = in.nextInt();
            if (rows < 0 || cols < 0) {
                throw new IllegalArgumentException("The range of numbers is broken");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Error: Invalid data format");
            return;
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
            return;
        }

        int[][] matrix = new int[rows][cols];
        Random randInt = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = randInt.nextInt(201) - 100;
            }
        }

        printMatrix(matrix);

        int index = searchRowIndex(matrix);

        printMaxRow(matrix, index);
    }

    static int searchRowIndex(int[][] matrix) {

        int sum, max = -1, index = -1;

        for (int i = 0; i < matrix.length; i++) {
            sum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] % 2 == 0) {
                    sum = -1;
                    break;
                }
                sum += Math.abs(matrix[i][j]);

            }

            if (sum > max) {
                max = sum;
                index = i;
            }
        }

        return index;
    }

    static void printMaxRow(int[][] matrix, int index) {

        if (index < 0) {
            System.out.println("\nRows with odd numbers were not found.");
            return;
        }

        int sum = 0;
        System.out.println("\nThe row number with the largest absolute sum of numbers: " + (index + 1));
        System.out.println("The row:");
        for (int i = 0; i < matrix[index].length; i++) {
            System.out.print(matrix[index][i] + "\t");
            sum += Math.abs(matrix[index][i]);
        }

        System.out.println("\nRow sum: " + sum);
    }

    static void printMatrix(int[][] matrix) {

        System.out.print("Matrix:");
        for (int[] ints : matrix) {
            System.out.println();
            for (int anInt : ints) {
                System.out.print(anInt + "\t");
            }
        }
    }
}