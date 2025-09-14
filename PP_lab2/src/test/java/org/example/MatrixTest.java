package org.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    // ------------------- Test searchRowIndex -------------------
    @Test
    void testSearchRowIndexEvenMatrix() {
        int[][] matrix = {
                {1, -2, 3},
                {-4, 5, -6},
                {7, 8, -9}
        };
        int expectedIndex = -1;
        assertEquals(expectedIndex, Matrix.searchRowIndex(matrix));
    }
    @Test
    void testSearchRowIndexNormalMatrix() {
        int[][] matrix = {
                {1, -5, 3},
                {-4, 5, -6},
                {7, 1, -9}
        };
        int expectedIndex = 2;
        assertEquals(expectedIndex, Matrix.searchRowIndex(matrix));
    }

    @Test
    void testSearchRowIndexEmptyMatrix() {
        int[][] empty = {};
        int expectedIndex = -1;
        assertEquals(expectedIndex, Matrix.searchRowIndex(empty));
    }

    @Test
    void testSearchRowIndexEqualSums() {
        int[][] matrix = {
                {1, -1},
                {1, -1},
                {-1, 1}
        };
        int expectedIndex = 0;
        assertEquals(expectedIndex, Matrix.searchRowIndex(matrix));
    }

    @Test
    void testSearchRowIndexSingleRow() {
        int[][] matrix = {
                {-5, 9, -3}
        };
        int expectedIndex = 0;
        assertEquals(expectedIndex, Matrix.searchRowIndex(matrix));
    }

    // ------------------- Test printMatrix -------------------
    @Test
    void testPrintNormalMatrix() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6}
        };

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Matrix.printMatrix(matrix);

        String output = outContent.toString();

        assertTrue(output.contains("1\t2\t3"));
        assertTrue(output.contains("4\t5\t6"));
        assertTrue(output.startsWith("Matrix:"));
    }

    // ------------------- Test printMaxRow -------------------
    @Test
    void testPrintMaxRow() {
        int[][] matrix = {
                {1, -2, 3},
                {-4, 5, -6},
                {7, 9, -9}
        };

        int index = Matrix.searchRowIndex(matrix); // индекс строки с максимальной суммой по модулю

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Matrix.printMaxRow(matrix, index);

        String output = outContent.toString();

        assertTrue(output.contains("The row number with the largest absolute sum of numbers: 3"));
        assertTrue(output.contains("7\t9\t-9"));
        assertTrue(output.contains("Row sum: 25"));
    }

    void testPrintMaxRowEmpty() {
        int[][] matrix = {
                {1, -2, 3},
                {-4, 5, -6},
                {7, 8, -9}
        };

        int index = -1; // индекс строки с максимальной суммой по модулю

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Matrix.printMaxRow(matrix, index);

        String output = outContent.toString();

        assertTrue(output.contains("\nRows with odd numbers were not found."));

    }
}
