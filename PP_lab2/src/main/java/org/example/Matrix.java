package org.example;

import java.util.Scanner;
import java.util.Random;

public class Matrix {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int rows, cols;

        System.out.println("Enter the number of rows of the matrix:");
        rows = in.nextInt();
        System.out.println("Enter the number of columns of the matrix:");
        cols = in.nextInt();

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

        int sum = 0, max = Integer.MIN_VALUE, index = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sum += Math.abs(matrix[i][j]);
            }

            if (sum > max) {
                max = sum;
                index = i;
            }

            sum = 0;
        }
        return index;
    }

    static void printMaxRow(int[][] matrix, int index) {

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