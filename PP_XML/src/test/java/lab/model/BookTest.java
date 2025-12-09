package lab.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookCreationAndGetters() {
        Book book = new Book(1, "Test Title", "Author", 2023, 100.0, "Cat", 10, 5);

        assertEquals(1, book.getId());
        assertEquals("Test Title", book.getTitle());
        assertEquals(100.0, book.getPrice());
        assertEquals(5, book.getAvailable());
    }

    @Test
    void testPropertiesUpdate() {
        Book book = new Book(1, "Title", "Author", 2023, 100.0, "Cat", 10, 5);

        book.setPrice(200.0);
        book.setAvailable(4);

        assertEquals(200.0, book.getPrice());
        assertEquals(4, book.getAvailable());
    }
}