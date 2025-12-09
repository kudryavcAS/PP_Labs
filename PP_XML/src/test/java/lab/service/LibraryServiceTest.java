package lab.service;

import lab.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void testLoadBooks() throws Exception {
        File xmlFile = tempDir.resolve("test_library.xml").toFile();
        File xsdFile = tempDir.resolve("test_library.xsd").toFile();

        createXsdFile(xsdFile);
        createXmlFile(xmlFile);

        LibraryService service = new LibraryService(xmlFile.getAbsolutePath(), xsdFile.getAbsolutePath());

        List<Book> books = service.loadBooks();

        assertEquals(2, books.size(), "Должно загрузиться ровно 2 книги");

        Book firstBook = books.getFirst();
        assertEquals("Test Book 1", firstBook.getTitle());
        assertEquals(10, firstBook.getTotal());
        assertEquals(5, firstBook.getAvailable());
    }

    private void createXmlFile(File file) throws Exception {
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                <library>
                    <book id="1" total="10" available="5">
                        <title>Test Book 1</title>
                        <author>Tester</author>
                        <year>2020</year>
                        <price>99.99</price>
                        <category>Test</category>
                    </book>
                    <book id="2" total="5" available="0">
                        <title>Test Book 2</title>
                        <author>Tester 2</author>
                        <year>2021</year>
                        <price>50.00</price>
                        <category>Test</category>
                    </book>
                </library>
                """;
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    private void createXsdFile(File file) throws Exception {
        String content = """
                <?xml version="1.0" encoding="UTF-8"?>
                <xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" xmlns:xs="http://www.w3.org/2001/XMLSchema">
                    <xs:element name="library">
                        <xs:complexType>
                            <xs:sequence>
                                <xs:element name="book" maxOccurs="unbounded" minOccurs="0">
                                    <xs:complexType>
                                        <xs:sequence>
                                            <xs:element type="xs:string" name="title"/>
                                            <xs:element type="xs:string" name="author"/>
                                            <xs:element type="xs:int" name="year"/>
                                            <xs:element type="xs:decimal" name="price"/>
                                            <xs:element type="xs:string" name="category"/>
                                        </xs:sequence>
                                        <xs:attribute type="xs:int" name="id" use="required"/>
                                        <xs:attribute type="xs:int" name="total" use="required"/>
                                        <xs:attribute type="xs:int" name="available" use="required"/>
                                    </xs:complexType>
                                </xs:element>
                            </xs:sequence>
                            <xs:anyAttribute processContents="skip"/>
                        </xs:complexType>
                    </xs:element>
                </xs:schema>
                """;
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}