package lab.textAlignment;

import lab.textStream.TextStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class TextAlignment {
    private final int maxLineLength;

    public TextAlignment(int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter input file name: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter output file name: ");
        String outputFile = scanner.nextLine();

        System.out.print("Enter maximum characters per line: ");
        int maxLineLength = scanner.nextInt();
        scanner.nextLine();

        scanner.close();

        TextAlignment alignment = new TextAlignment(maxLineLength);
        alignment.processFile(inputFile, outputFile);
    }

    public void processFile(String inputFile, String outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            StringBuilder outputContent = new StringBuilder();

            List<String> currentParagraph = new ArrayList<>();

            while (true) {
                String line = reader.readLine();

                if (line == null || line.trim().isEmpty()) {
                    // Пустая строка или конец файла - заканчиваем абзац
                    if (!currentParagraph.isEmpty()) {
                        String formattedParagraph = formatParagraph(currentParagraph);
                        if (outputContent.length() > 0) {
                            outputContent.append("\n"); // Просто перенос строки между абзацами
                        }
                        outputContent.append(formattedParagraph);
                        currentParagraph.clear();
                    }

                    if (line == null) break; // Конец файла
                } else {
                    // Непустая строка - добавляем в текущий абзац
                    currentParagraph.add(line.trim());
                }
            }

            // Записываем результат
            TextStream.writeToFile(outputFile, outputContent.toString());
            System.out.println("Text aligned successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String formatParagraph(List<String> paragraphLines) {
        // Объединяем все строки абзаца в один текст
        String paragraphText = String.join(" ", paragraphLines);
        List<String> words = splitIntoWords(paragraphText);

        List<String> formattedLines = new ArrayList<>();
        List<String> currentLineWords = new ArrayList<>();
        int currentLineLength = 0;
        boolean firstLine = true;

        for (String word : words) {
            int wordLength = word.length();

            if (currentLineWords.isEmpty()) {
                currentLineWords.add(word);
                currentLineLength = wordLength;
            } else {
                if (currentLineLength + 1 + wordLength <= maxLineLength) {
                    currentLineWords.add(word);
                    currentLineLength += 1 + wordLength;
                } else {
                    formattedLines.add(createFormattedLine(currentLineWords, firstLine, false));
                    firstLine = false;
                    currentLineWords.clear();
                    currentLineWords.add(word);
                    currentLineLength = wordLength;
                }
            }
        }

        if (!currentLineWords.isEmpty()) {
            formattedLines.add(createFormattedLine(currentLineWords, firstLine, true));
        }

        return String.join("\n", formattedLines);
    }

    private List<String> splitIntoWords(String text) {
        List<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (currentWord.length() > 0) {
                    words.add(currentWord.toString());
                    currentWord = new StringBuilder();
                }
            } else {
                currentWord.append(c);
            }
        }

        if (currentWord.length() > 0) {
            words.add(currentWord.toString());
        }

        return words;
    }

    private String createFormattedLine(List<String> words, boolean firstLine, boolean isLastLine) {
        StringBuilder line = new StringBuilder();

        // Добавляем 4 пробела для красной строки если это первая строка абзаца
        if (firstLine) {
            for (int i = 0; i < 4; i++) {
                line.append(" ");
            }
        }

        if (words.size() == 1 || isLastLine) {
            // Одно слово или последняя строка - просто соединяем пробелами
            line.append(String.join(" ", words));
        } else {
            // Равномерно распределяем пробелы
            int totalChars = words.stream().mapToInt(String::length).sum();
            int availableLength = maxLineLength - (firstLine ? 4 : 0);
            int totalSpaces = availableLength - totalChars;
            int spaceSlots = words.size() - 1;

            int baseSpaces = totalSpaces / spaceSlots;
            int extraSpaces = totalSpaces % spaceSlots;

            for (int i = 0; i < words.size(); i++) {
                line.append(words.get(i));
                if (i < words.size() - 1) {
                    int spacesToAdd = baseSpaces + (i < extraSpaces ? 1 : 0);
                    for (int j = 0; j < spacesToAdd; j++) {
                        line.append(" ");
                    }
                }
            }
        }

        return line.toString();
    }
}