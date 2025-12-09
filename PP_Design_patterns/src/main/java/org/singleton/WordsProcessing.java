package org.singleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordsProcessing {

    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+\\b", Pattern.UNICODE_CHARACTER_CLASS);
    private static WordsProcessing instance;
    private final String line;

    private WordsProcessing(String _line) {
        line = _line;
    }

    public static WordsProcessing getInstance(String line) {
        if (instance == null) {
            instance = new WordsProcessing(line);
        }
        return instance;
    }
     String processingLine() {
        if (line == null || line.isEmpty()) {
            return line;
        }

        Matcher allWords = WORD_PATTERN.matcher(line);

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