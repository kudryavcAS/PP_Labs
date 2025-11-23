package lab.textStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TextStream {
    private static BufferedReader reader;
    private static String currentFileName = "";

    public static void writeToFile(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(text);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static String readFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (!firstLine) {
                    content.append(" ");
                }
                content.append(line);
                firstLine = false;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return content.toString();
    }

    public static void openFileForParagraphReading(String fileName) {
        try {
            if (reader != null) {
                reader.close();
            }
            reader = new BufferedReader(new FileReader(fileName));
            currentFileName = fileName;
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }

    public static String readParagraph() {
        if (reader == null) {
            return null;
        }

        try {
            StringBuilder paragraph = new StringBuilder();
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (paragraph.length() > 0) {
                        return paragraph.toString().trim();
                    }
                    continue;
                }

                if (!firstLine) {
                    paragraph.append(" ");
                }
                paragraph.append(line.trim());
                firstLine = false;
            }

            // Возвращаем последний абзац (если файл закончился)
            if (paragraph.length() > 0) {
                return paragraph.toString().trim();
            }

            // Файл полностью прочитан
            closeFile();
            return null;

        } catch (IOException e) {
            System.err.println("Error reading paragraph: " + e.getMessage());
            return null;
        }
    }

    // Метод для закрытия файла
    public static void closeFile() {
        if (reader != null) {
            try {
                reader.close();
                reader = null;
                setCurrentFileName("");
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }

    public static String getCurrentFileName() {
        return currentFileName;
    }

    public static void setCurrentFileName(String currentFileName) {
        TextStream.currentFileName = currentFileName;
    }
}