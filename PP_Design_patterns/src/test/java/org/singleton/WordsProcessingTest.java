package org.singleton;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordsProcessingTest {
    @Test
    void testProcessingLineSimple() {
        String line = "jpoo joo";
        String expected = "Jpoo Joo";
        WordsProcessing a = WordsProcessing.getInstance(line);
        String actual = a.processingLine();
        assertEquals(expected, actual);
    }


    @Test
    void testProcessingLineDelimeters() {
        String line = "And did, yOU, cover the code, with unit tests....?";
        String expected = "And Did, YOU, Cover The Code, With Unit Tests....?";
        String actual = WordsProcessing.getInstance(line).processingLine();
        assertEquals(expected, actual);
    }

    @Test
    void testProcessingLineRussian() {
        String line = "Не пишутся тесты - do refactoring!";
        String expected = "Не Пишутся Тесты - Do Refactoring!";
        String actual = WordsProcessing.getInstance(line).processingLine();
        assertEquals(expected, actual);
    }


}