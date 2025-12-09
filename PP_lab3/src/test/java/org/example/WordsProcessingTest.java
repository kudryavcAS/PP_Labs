package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordsProcessingTest {
    @Test
    void testProcessingLineSimple() {
        String line = "jpoo joo";
        String expected = "Jpoo Joo";
        String actual = WordsProcessing.processingLine(line);
        assertEquals(expected, actual);
    }

    @Test
    void testProcessingLineEmpty() {
        String line = "";
        String expected = "";
        String actual = WordsProcessing.processingLine(line);
        assertEquals(expected, actual);
    }

    @Test
    void testProcessingLineDelimeters() {
        String line = "And did, yOU, cover the code, with unit tests....?";
        String expected = "And Did, YOU, Cover The Code, With Unit Tests....?";
        String actual = WordsProcessing.processingLine(line);
        assertEquals(expected, actual);
    }

    @Test
    void testProcessingLineRussian() {
        String line = "Не пишутся тесты - do refactoring!";
        String expected = "Не Пишутся Тесты - Do Refactoring!";
        String actual = WordsProcessing.processingLine(line);
        assertEquals(expected, actual);
    }


}