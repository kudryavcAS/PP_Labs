package org.example;

import java.io.*;

public class FileProcessing {
    private String text;
    private String formattedText;

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public void readFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            text = content.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading the file: " + e.getMessage(), e);
        }
    }

    public void writeToFile(String filename, String formattedText) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(formattedText);
        } catch (IOException e) {
            System.out.println("Error when writing a file:" + e.getMessage());
        }
    }

}
