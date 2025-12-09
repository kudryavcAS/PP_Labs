package lab.textFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextStream {
    private static final String LINE_SEPARATOR = " ";
    private static final String EMPTY_STRING = "";

    private BufferedReader reader;
    private String currentFileName = EMPTY_STRING;

    public TextStream() {
    }

    public void writeToFile(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(text);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public String readFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;
            while ((line = fileReader.readLine()) != null) {
                if (!firstLine) {
                    content.append(LINE_SEPARATOR);
                }
                content.append(line);
                firstLine = false;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return content.toString();
    }

    public void openFileForParagraphReading(String fileName) {
        try {
            closeFile();
            reader = new BufferedReader(new FileReader(fileName));
            currentFileName = fileName;
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }

    public String readParagraph() {
        if (reader == null) {
            return null;
        }

        try {
            StringBuilder paragraph = new StringBuilder();
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (!paragraph.isEmpty()) {
                        return paragraph.toString().trim();
                    }
                    continue;
                }

                if (!firstLine) {
                    paragraph.append(LINE_SEPARATOR);
                }
                paragraph.append(line.trim());
                firstLine = false;
            }

            if (!paragraph.isEmpty()) {
                return paragraph.toString().trim();
            }

            closeFile();
            return null;

        } catch (IOException e) {
            System.err.println("Error reading paragraph: " + e.getMessage());
            return null;
        }
    }

    public void closeFile() {
        if (reader != null) {
            try {
                reader.close();
                reader = null;
                currentFileName = EMPTY_STRING;
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public boolean isFileOpen() {
        return reader != null;
    }
}