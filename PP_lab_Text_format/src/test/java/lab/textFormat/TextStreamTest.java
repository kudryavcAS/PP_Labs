package lab.textFormat;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import io.qameta.allure.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Text Processing")
@Feature("TextStream File Operations")
class TextStreamTest {

    @TempDir
    Path tempDir;

    private TextStream textStream;
    private Path testFile;
    private Path outputFile;

    @BeforeEach
    void setUp() throws IOException {
        textStream = new TextStream();
        testFile = tempDir.resolve("test_input.txt");
        outputFile = tempDir.resolve("test_output.txt");
    }

    @Test
    @DisplayName("Should write and read text to file")
    @Severity(SeverityLevel.CRITICAL)
    void testWriteAndReadFile() {
        String testText = "Hello World!\nThis is a test.";

        textStream.writeToFile(testFile.toString(), testText);
        String result = textStream.readFromFile(testFile.toString());

        assertEquals("Hello World! This is a test.", result);
    }

    @Test
    @DisplayName("Should handle empty file")
    @Severity(SeverityLevel.NORMAL)
    void testReadEmptyFile() throws IOException {
        Files.writeString(testFile, "");

        String result = textStream.readFromFile(testFile.toString());

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should read paragraph from multi-paragraph file")
    @Severity(SeverityLevel.CRITICAL)
    void testReadParagraph() throws IOException {
        String content = "First paragraph line 1\nFirst paragraph line 2\n\nSecond paragraph";
        Files.writeString(testFile, content);

        textStream.openFileForParagraphReading(testFile.toString());

        String firstParagraph = textStream.readParagraph();
        String secondParagraph = textStream.readParagraph();
        String thirdParagraph = textStream.readParagraph();

        assertEquals("First paragraph line 1 First paragraph line 2", firstParagraph);
        assertEquals("Second paragraph", secondParagraph);
        assertNull(thirdParagraph);
    }

    @Test
    @DisplayName("Should handle file with multiple empty lines")
    @Severity(SeverityLevel.NORMAL)
    void testReadParagraphWithMultipleEmptyLines() throws IOException {
        String content = "Paragraph 1\n\n\n\nParagraph 2";
        Files.writeString(testFile, content);

        textStream.openFileForParagraphReading(testFile.toString());

        String firstParagraph = textStream.readParagraph();
        String secondParagraph = textStream.readParagraph();

        assertEquals("Paragraph 1", firstParagraph);
        assertEquals("Paragraph 2", secondParagraph);
    }

    @Test
    @DisplayName("Should handle non-existent file gracefully")
    @Severity(SeverityLevel.NORMAL)
    void testReadNonExistentFile() {
        String result = textStream.readFromFile("non_existent_file.txt");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should write to file and verify content")
    @Severity(SeverityLevel.CRITICAL)
    void testWriteToFile() throws IOException {
        String expectedContent = "Test content with special chars: !@#$%^&*()";

        textStream.writeToFile(outputFile.toString(), expectedContent);

        String actualContent = Files.readString(outputFile);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("Should track current file name")
    @Severity(SeverityLevel.MINOR)
    void testCurrentFileName() throws IOException {
        Files.writeString(testFile, "test content");

        textStream.openFileForParagraphReading(testFile.toString());

        assertEquals(testFile.toString(), textStream.getCurrentFileName());
        assertTrue(textStream.isFileOpen());

        textStream.closeFile();

        assertFalse(textStream.isFileOpen());
        assertTrue(textStream.getCurrentFileName().isEmpty());
    }

    @Test
    @DisplayName("Should handle sequential file operations")
    @Severity(SeverityLevel.NORMAL)
    void testSequentialFileOperations() throws IOException {
        Path file1 = tempDir.resolve("file1.txt");
        Path file2 = tempDir.resolve("file2.txt");

        Files.writeString(file1, "Content 1");
        Files.writeString(file2, "Content 2");

        textStream.openFileForParagraphReading(file1.toString());
        String content1 = textStream.readParagraph();

        textStream.openFileForParagraphReading(file2.toString());
        String content2 = textStream.readParagraph();

        assertEquals("Content 1", content1);
        assertEquals("Content 2", content2);
    }

    @AfterEach
    void tearDown() {
        textStream.closeFile();
    }
}