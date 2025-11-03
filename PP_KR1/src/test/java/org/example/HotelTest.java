package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.qameta.allure.*;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Hotel Management")
@Feature("Hotel Object")
class HotelTest {

    @Test
    @DisplayName("Create hotel with valid parameters")
    @Story("Hotel Creation")
    @Severity(SeverityLevel.CRITICAL)
    void testHotelCreationWithValidParameters() {
        Hotel hotel = new Hotel("Минск", "Hilton", 5);

        assertEquals("Минск", hotel.getCity());
        assertEquals("Hilton", hotel.getName());
        assertEquals(5, hotel.getStars());
    }

    @Test
    @DisplayName("Create hotel from valid file line")
    @Story("File Line Parsing")
    @Severity(SeverityLevel.CRITICAL)
    void testHotelCreationFromValidFileLine() {
        String fileLine = "Боровляны Hilton 5";
        Hotel hotel = new Hotel(fileLine);

        assertEquals("Боровляны", hotel.getCity());
        assertEquals("Hilton", hotel.getName());
        assertEquals(5, hotel.getStars());
    }

    @Test
    @DisplayName("Fail creation from invalid file line format")
    @Story("File Line Parsing")
    @Severity(SeverityLevel.CRITICAL)
    void testHotelCreationFromInvalidFileLine() {
        String invalidLine = "ТолькоДваПоля";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Hotel(invalidLine)
        );

        assertTrue(exception.getMessage().contains("Неверный формат строки"));
    }

    @Test
    @DisplayName("Two identical hotels should be equal")
    @Story("Hotel Equality")
    @Severity(SeverityLevel.NORMAL)
    void testHotelsEquality() {
        Hotel hotel1 = new Hotel("Минск", "Hilton", 5);
        Hotel hotel2 = new Hotel("Минск", "Hilton", 5);

        assertTrue(hotel1.equals(hotel2));
        assertEquals(hotel1.hashCode(), hotel2.hashCode());
    }

    @Test
    @DisplayName("Different hotels should not be equal")
    @Story("Hotel Equality")
    @Severity(SeverityLevel.NORMAL)
    void testHotelsInequality() {
        Hotel hotel1 = new Hotel("Минск", "Hilton", 5);
        Hotel hotel2 = new Hotel("Гродно", "Hilton", 5);
        Hotel hotel3 = new Hotel("Минск", "Marriott", 5);
        Hotel hotel4 = new Hotel("Минск", "Hilton", 4);

        assertFalse(hotel1.equals(hotel2));
        assertFalse(hotel1.equals(hotel3));
        assertFalse(hotel1.equals(hotel4));
    }

    @Test
    @DisplayName("Hotel should not equal null or different class")
    @Story("Hotel Equality")
    @Severity(SeverityLevel.NORMAL)
    void testHotelEqualityWithNullAndDifferentClass() {
        Hotel hotel = new Hotel("Минск", "Hilton", 5);

        assertFalse(hotel.equals(null));
        assertFalse(hotel.equals("Минск Hilton 5"));
    }

    @Test
    @DisplayName("ToString returns correct format")
    @Story("Hotel Representation")
    @Severity(SeverityLevel.MINOR)
    void testToString() {
        Hotel hotel = new Hotel("Минск", "Hilton", 5);
        String result = hotel.toString();

        assertTrue(result.contains("Минск"));
        assertTrue(result.contains("Hilton"));
        assertTrue(result.contains("5"));
    }
}