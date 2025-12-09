package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PoemSorterTest {
    @TempDir
    Path tempDir;

    @Test
    void testReadPoemFile() throws IOException {

        File testFile = tempDir.resolve("test_poem.txt").toFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(testFile));
        writer.write("First line;\n");
        writer.write("Second line:");
        writer.newLine();
        writer.write("Third line.");
        writer.close();

        List<String> result = PoemSorter.readPoemFile(testFile.getAbsolutePath());

        assertEquals(3, result.size());
        assertEquals("First line;", result.get(0));
        assertEquals("Second line:", result.get(1));
        assertEquals("Third line.", result.get(2));
    }

    @Test
    void testReadPoemFileEmptyFile() throws IOException {
        File emptyFile = tempDir.resolve("empty.txt").toFile();
        emptyFile.createNewFile();

        List<String> result = PoemSorter.readPoemFile(emptyFile.getAbsolutePath());

        assertTrue(result.isEmpty());
    }

    @Test
    void testReadPoemFileFileNotFound() {
        File nonExistentFile = tempDir.resolve("nofile.txt").toFile();

        assertThrows(IOException.class, () -> {
            PoemSorter.readPoemFile(nonExistentFile.getAbsolutePath());
        });
    }

    @Test
    void testSortPoem() {
        List<String> input = Arrays.asList(
                "has most",
                "My",
                "uncle",
                "he forced one to",
                "uncle",
                "honest principles: "
        );

        PoemSorter.sortPoem(input);

        assertEquals(6, input.size());
        assertEquals("My", input.get(0));
        assertEquals("uncle", input.get(1));
        assertEquals("uncle", input.get(2));
        assertEquals("has most", input.get(3));
        assertEquals("he forced one to", input.get(4));
        assertEquals("honest principles: ", input.get(5));
    }

    @Test
    void testSortPoemEmptyList() {
        List<String> emptyList = new ArrayList<>();
        PoemSorter.sortPoem(emptyList);

        assertTrue(emptyList.isEmpty());
    }

    @Test
    void testSortPoemSame() {
        List<String> input = Arrays.asList(
                "Rick",
                "Roll",
                "Never",
                "Gonna"
        );

        PoemSorter.sortPoem(input);

        assertEquals(4, input.size());
        assertEquals("Rick", input.get(0));
        assertEquals("Roll", input.get(1));
        assertEquals("Never", input.get(2));
        assertEquals("Gonna", input.get(3));
    }

    @Test
    void testWritePoemToFile() throws IOException {
        List<String> poem = Arrays.asList(
                "First line",
                "Second line",
                "Third line"
        );
        File outputFile = tempDir.resolve("poem_output.txt").toFile();

        PoemSorter.writePoemToFile(poem, outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        List<String> fileContent = Files.readAllLines(outputFile.toPath());
        assertEquals(3, fileContent.size());
        assertEquals("First line", fileContent.get(0));
        assertEquals("Second line", fileContent.get(1));
        assertEquals("Third line", fileContent.get(2));
    }

    @Test
    void testWritePoemToFileEmptyListStrict() throws IOException {
        List<String> emptyPoem = new ArrayList<>();
        File outputFile = tempDir.resolve("empty.txt").toFile();

        PoemSorter.writePoemToFile(emptyPoem, outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertEquals(0, outputFile.length());

        List<String> fileContent = Files.readAllLines(outputFile.toPath());
        assertTrue(fileContent.isEmpty());
    }
}