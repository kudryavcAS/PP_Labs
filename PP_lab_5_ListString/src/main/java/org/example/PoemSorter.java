package org.example;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class PoemSorter {
    public static void main(String[] args) {
        String inputFile = "poem.txt";
        String outputFile = "sorted_poem.txt";

        try {
            List<String> poem = readPoemFile(inputFile);

            System.out.println("The original poem:");
            outPoem(poem);

            sortPoem(poem);
            System.out.println("\nSorted poem:");
            outPoem(poem);

            writePoemToFile(poem, outputFile);
        } catch (IOException exc) {
            System.err.println("Error when working with files: " + exc.getMessage());
        }
    }

    public static List<String> readPoemFile(String inputFile) throws IOException {
        List<String> poem = new ArrayList<String>();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        while ((line = reader.readLine()) != null) {
            poem.add(line);
        }
        reader.close();

        return poem;
    }

    public static void outPoem(List<String> poem) {
        for (String line : poem) {
            System.out.println(line);
        }
    }

    public static void sortPoem(List<String> poem) {
        poem.sort(Comparator.comparingInt(String::length));
    }

    public static void writePoemToFile(List<String> poem, String outputFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (String line : poem) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }

}

