package lab.textFormat;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class TextAlignment {
    private static final int DEFAULT_FIRST_LINE_INDENT = 4;
    private static final int SINGLE_SPACE_LENGTH = 1;
    private static final int MIN_LINE_LENGTH = 1;

    private final int maxLineLength;
    private final TextStream textStream;

    public TextAlignment(int maxLineLength) {
        this(maxLineLength, new TextStream());
    }

    public TextAlignment(int maxLineLength, TextStream textStream) {
        if (maxLineLength < MIN_LINE_LENGTH) {
            throw new IllegalArgumentException("Max line length must be at least " + MIN_LINE_LENGTH);
        }
        this.maxLineLength = maxLineLength;
        this.textStream = textStream;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter input file name: ");
        String inputFile = scanner.nextLine();

        System.out.println("Enter output file name: ");
        String outputFile = scanner.nextLine();

        System.out.println("Enter maximum characters per line: ");
        int maxLineLength = scanner.nextInt();
        scanner.nextLine();

        scanner.close();

        TextAlignment alignment = new TextAlignment(maxLineLength);
        alignment.processFile(inputFile, outputFile);
    }

    public void processFile(String inputFile, String outputFile) {
        try {
            textStream.openFileForParagraphReading(inputFile);
            StringBuilder outputContent = new StringBuilder();
            String paragraph;
            boolean firstParagraph = true;

            while ((paragraph = textStream.readParagraph()) != null) {
                if (!paragraph.trim().isEmpty()) {
                    String formattedParagraph = formatParagraph(paragraph);
                    if (!firstParagraph) {
                        outputContent.append("\n");
                    }
                    outputContent.append(formattedParagraph);
                    firstParagraph = false;
                }
            }

            textStream.closeFile();
            textStream.writeToFile(outputFile, outputContent.toString());
            System.out.println("Text aligned successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            textStream.closeFile();
        }
    }

    String formatParagraph(String paragraphText) {
        List<String> words = splitIntoWords(paragraphText);
        List<String> formattedLines = new ArrayList<>();
        List<String> currentLineWords = new ArrayList<>();

        int currentLineLength = 0;
        boolean firstLine = true;

        for (String word : words) {
            int wordLength = word.length();

            int availableLength = maxLineLength - (firstLine ? DEFAULT_FIRST_LINE_INDENT : 0);

            if (currentLineWords.isEmpty()) {
                currentLineWords.add(word);
                currentLineLength = wordLength;
            } else {
                if (currentLineLength + SINGLE_SPACE_LENGTH + wordLength <= availableLength) {
                    currentLineWords.add(word);
                    currentLineLength += SINGLE_SPACE_LENGTH + wordLength;
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

    List<String> splitIntoWords(String text) {
        List<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (!currentWord.isEmpty()) {
                    words.add(currentWord.toString());
                    currentWord = new StringBuilder();
                }
            } else {
                currentWord.append(c);
            }
        }

        if (!currentWord.isEmpty()) {
            words.add(currentWord.toString());
        }

        return words;
    }

    String createFormattedLine(List<String> words, boolean firstLine, boolean isLastLine) {
        StringBuilder line = new StringBuilder();

        if (firstLine) {
            line.append(" ".repeat(DEFAULT_FIRST_LINE_INDENT));
        }

        if (words.size() == 1 || isLastLine) {
            line.append(String.join(" ", words));
        } else {
            int totalChars = words.stream().mapToInt(String::length).sum();
            int availableLength = maxLineLength - (firstLine ? DEFAULT_FIRST_LINE_INDENT : 0);
            int totalSpaces = availableLength - totalChars;
            int spaceSlots = words.size() - 1;

            int baseSpaces = totalSpaces / spaceSlots;
            int extraSpaces = totalSpaces % spaceSlots;

            for (int i = 0; i < words.size(); i++) {
                line.append(words.get(i));
                if (i < words.size() - 1) {
                    int spacesToAdd = baseSpaces + (i < extraSpaces ? 1 : 0);
                    line.append(" ".repeat(Math.max(0, spacesToAdd)));
                }
            }
        }

        return line.toString();
    }
}