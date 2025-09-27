package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextCipherTest{
    @Test
    void testEncryptTextSimple() {
        String line = "Simple test";
        String expected = "Sp silttmee";
        String actual = TextCipher.encryptText(line);
        assertEquals(expected, actual);
    }
@Test
    void testEncryptTextEmpty() {
        String line = "";
        String expected = "";
        String actual = TextCipher.encryptText(line);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptTextSameShort() {
        String line = "no";
        String expected = "no";
        String actual = TextCipher.encryptText(line);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptTextSameLong() {
        String line = "oooooooooooooo";
        String expected = "oooooooooooooo";
        String actual = TextCipher.encryptText(line);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptTextSentence() {
        String line = "Sit down and think";
        String expected = "S wa iidnntnto dhk";
        String actual = TextCipher.encryptText(line);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptTextMultiLine() {
        String line = "Git Bash\ncd /c/pp_labs";
        String expected = "G sc/plsiBhdcpata\n" +
                " /_b";
        String actual = TextCipher.encryptText(line);
        assertEquals(expected, actual);
    }


}