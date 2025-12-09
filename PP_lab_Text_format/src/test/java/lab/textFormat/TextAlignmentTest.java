package lab.textFormat;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import io.qameta.allure.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Text Processing")
@Feature("Text Alignment and Formatting")
class TextAlignmentTest {

    @TempDir
    Path tempDir;

    private Path inputFile;
    private Path outputFile;
    private TextAlignment textAlignment;
    private TextStream textStream;

    @BeforeEach
    void setUp() throws IOException {
        inputFile = tempDir.resolve("input.txt");
        outputFile = tempDir.resolve("output.txt");
        textStream = new TextStream();
    }

    @Test
    @DisplayName("Should format single paragraph with first line indent")
    @Severity(SeverityLevel.CRITICAL)
    void testFormatSingleParagraph() {
        textAlignment = new TextAlignment(30, textStream);
        String paragraph = "This is a test paragraph with multiple words that should be formatted.";

        String result = textAlignment.formatParagraph(paragraph);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.startsWith("    ")),
                () -> assertTrue(result.contains("\n"))
        );
    }

    @Test
    @DisplayName("Should split words correctly")
    @Severity(SeverityLevel.CRITICAL)
    void testSplitIntoWords() {
        textAlignment = new TextAlignment(20, textStream);

        String text = "This is   a    test   with   multiple   spaces";
        List<String> words = textAlignment.splitIntoWords(text);

        assertAll(
                () -> assertEquals(7, words.size()),
                () -> assertEquals("This", words.get(0)),
                () -> assertEquals("is", words.get(1)),
                () -> assertEquals("a", words.get(2)),
                () -> assertEquals("test", words.get(3)),
                () -> assertEquals("with", words.get(4)),
                () -> assertEquals("multiple", words.get(5)),
                () -> assertEquals("spaces", words.get(6))
        );
    }

    @Test
    @DisplayName("Should handle empty paragraph")
    @Severity(SeverityLevel.NORMAL)
    void testFormatEmptyParagraph() {
        textAlignment = new TextAlignment(30, textStream);

        String result = textAlignment.formatParagraph("");

        assertEquals("", result);
    }

    @Test
    @DisplayName("Should create formatted line with proper spacing")
    @Severity(SeverityLevel.CRITICAL)
    void testCreateFormattedLine() {
        textAlignment = new TextAlignment(20, textStream);

        List<String> words = List.of("This", "is", "test");

        String firstLine = textAlignment.createFormattedLine(words, true, false);
        String lastLine = textAlignment.createFormattedLine(words, false, true);

        assertTrue(firstLine.startsWith("    "));
        assertEquals(20, firstLine.length());
        assertEquals("This is test", lastLine);
    }

    @Test
    @DisplayName("Should process file with multiple paragraphs")
    @Severity(SeverityLevel.CRITICAL)
    void testProcessFileWithMultipleParagraphs() throws IOException {
        textAlignment = new TextAlignment(40, textStream);
        String content = """
                First paragraph with multiple lines
                that should be formatted together.
                
                Second paragraph that is separate
                from the first one.
                
                Third and final paragraph.""";

        Files.writeString(inputFile, content);

        textAlignment.processFile(inputFile.toString(), outputFile.toString());

        assertTrue(Files.exists(outputFile));

        String outputContent = Files.readString(outputFile);
        assertNotNull(outputContent);
        assertFalse(outputContent.isEmpty());
        assertTrue(outputContent.contains("\n"));
    }

    @Test
    @DisplayName("Should handle very long words")
    @Severity(SeverityLevel.NORMAL)
    void testFormatParagraphWithLongWords() {
        textAlignment = new TextAlignment(10, textStream);
        String paragraph = "abcde fghij klmno";

        String result = textAlignment.formatParagraph(paragraph);

        assertNotNull(result);
        assertTrue(result.split("\n").length >= 3);
    }

    @Test
    @DisplayName("Should format line with single word")
    @Severity(SeverityLevel.NORMAL)
    void testSingleWordFormatting() {
        textAlignment = new TextAlignment(20, textStream);
        List<String> words = List.of("Hello");

        String result = textAlignment.createFormattedLine(words, true, false);

        assertEquals("    Hello", result);
    }

    @Test
    @DisplayName("Should handle edge case with exact line length")
    @Severity(SeverityLevel.NORMAL)
    void testExactLineLength() {
        textAlignment = new TextAlignment(15, textStream);
        String paragraph = "Hello World";

        String result = textAlignment.formatParagraph(paragraph);

        assertNotNull(result);
        assertTrue(result.length() <= 15 + 4);
    }

    @Test
    @DisplayName("Should process non-existent input file gracefully")
    @Severity(SeverityLevel.NORMAL)
    void testProcessNonExistentFile() {
        textAlignment = new TextAlignment(30, textStream);

        assertDoesNotThrow(() -> {
            textAlignment.processFile("non_existent.txt", outputFile.toString());
        });
    }

    @Test
    @DisplayName("Should use default constructor")
    @Severity(SeverityLevel.MINOR)
    void testDefaultConstructor() {
        TextAlignment alignment = new TextAlignment(25);

        assertNotNull(alignment);
    }

    @Test
    @DisplayName("Should throw exception for invalid line length")
    @Severity(SeverityLevel.NORMAL)
    void testInvalidLineLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TextAlignment(0, textStream);
        });
    }

    @Nested
    @DisplayName("Different Line Length Tests")
    class LineLengthTests {

        @Test
        @DisplayName("Should format with very short line length")
        @Severity(SeverityLevel.MINOR)
        void testVeryShortLineLength() {
            textAlignment = new TextAlignment(5, textStream);
            String paragraph = "Hello World";

            String result = textAlignment.formatParagraph(paragraph);

            assertNotNull(result);
            assertTrue(result.split("\n").length > 1);
        }

        @Test
        @DisplayName("Should format with very long line length")
        @Severity(SeverityLevel.MINOR)
        void testVeryLongLineLength() {
            textAlignment = new TextAlignment(100, textStream);
            String paragraph = "This is a relatively short paragraph that should fit in one line";

            String result = textAlignment.formatParagraph(paragraph);

            assertNotNull(result);
            assertTrue(result.split("\n").length <= 2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with real TextStream instance")
        @Severity(SeverityLevel.CRITICAL)
        void testWithRealTextStream() throws IOException {
            TextStream realStream = new TextStream();
            TextAlignment alignment = new TextAlignment(30, realStream);

            Files.writeString(inputFile, "Test paragraph content");

            alignment.processFile(inputFile.toString(), outputFile.toString());

            assertTrue(Files.exists(outputFile));
        }

        @Test
        @DisplayName("Should handle multiple TextAlignment instances")
        @Severity(SeverityLevel.NORMAL)
        void testMultipleInstances() {
            TextAlignment alignment1 = new TextAlignment(20);
            TextAlignment alignment2 = new TextAlignment(40);

            String paragraph = "This is a longer text that will be formatted differently based on line length";

            String result1 = alignment1.formatParagraph(paragraph);
            String result2 = alignment2.formatParagraph(paragraph);

            assertNotNull(result1);
            assertNotNull(result2);

            int lines1 = result1.split("\n").length;
            int lines2 = result2.split("\n").length;

            assertTrue(lines1 > lines2, "Shorter line length should produce more lines");
        }
    }
}