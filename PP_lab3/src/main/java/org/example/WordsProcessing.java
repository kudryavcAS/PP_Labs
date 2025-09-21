package org.example;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsProcessing {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter the lines (to complete, enter an empty line):");
        ArrayList<String> lines = new ArrayList<>();

        while (true) {
            String line = in.nextLine();
            if (line.isEmpty()) {
                break;
            }

            lines.add(line);
        }
        in.close();

        System.out.println("Lines with the first capital letters:");
        for (String line : lines) {
            System.out.println(processingLine(line));
        }
    }

    static String processingLine(String line) {
        if (line == null || line.isEmpty()) {
            return line;
        }
        Pattern wordPattern = Pattern.compile("\\p{L}+\\b", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher allWords = wordPattern.matcher(line);

        StringBuffer result = new StringBuffer();

        while (allWords.find()) {
            String word = allWords.group();
            String extracted = word.substring(0, 1).toUpperCase() + word.substring(1);

            allWords.appendReplacement(result, extracted);
        }

        allWords.appendTail(result);
        return result.toString();
    }


}