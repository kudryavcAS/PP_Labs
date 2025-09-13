package org.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    // ------------------- Тест searchRowIndex -------------------
    @Test
    void testSearchRowIndexNormalMatrix() {
        int[][] matrix = {
                {1, -2, 3},      // сумма по модулю = 6
                {-4, 5, -6},     // сумма по модулю = 15
                {7, 8, -9}       // сумма по модулю = 24
        };
        int expectedIndex = 2;
        assertEquals(expectedIndex, Matrix.searchRowIndex(matrix));
    }

    @Test
    void testSearchRowIndexEmptyMatrix() {
        int[][] empty = {};
        int expectedIndex = 0;
        assertEquals(expectedIndex, Matrix.searchRowIndex(empty));
    }

    @Test
    void testSearchRowIndexEqualSums() {
        int[][] matrix = {
                {1, -1},          // сумма = 2
                {2, -2},          // сумма = 4
                {-2, 2}           // сумма = 4
        };
        int expectedIndex = 1;
        assertEquals(expectedIndex, Matrix.searchRowIndex(matrix));
    }

    @Test
    void testSearchRowIndexSingleRow() {
        int[][] matrix = {
                {-5, 10, -3}      // сумма = 18
        };
        int expectedIndex = 0;
        assertEquals(expectedIndex, Matrix.searchRowIndex(matrix));
    }

    // ------------------- Тест printMatrix -------------------
    @Test
    void testPrintMatrix() {
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

    // ------------------- Тест printMaxRow -------------------
    @Test
    void testPrintMaxRow() {
        int[][] matrix = {
                {1, -2, 3},
                {-4, 5, -6},
                {7, 8, -9}
        };

        int index = Matrix.searchRowIndex(matrix); // индекс строки с максимальной суммой по модулю

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Matrix.printMaxRow(matrix, index);

        String output = outContent.toString();

        assertTrue(output.contains("The row number with the largest absolute sum of numbers: 3"));
        assertTrue(output.contains("7\t8\t-9"));
        assertTrue(output.contains("Row sum: 24"));
    }
}
