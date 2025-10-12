package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class OrderInternationalizationTest {

    @Test
    void testGetLocaleRuBY() {
        Locale result = OrderInternationalization.getLocale(1);
        assertEquals("ru", result.getLanguage());
        assertEquals("BY", result.getCountry());
        assertEquals(new Locale("ru", "BY"), result);
    }

    @Test
    void testGetLocaleUS() {
        Locale result = OrderInternationalization.getLocale(2);
        assertEquals(Locale.US, result);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    void testGetLocaleGermany() {
        Locale result = OrderInternationalization.getLocale(3);
        assertEquals(Locale.GERMANY, result);
        assertEquals("de", result.getLanguage());
        assertEquals("DE", result.getCountry());
    }

    @Test
    void testGetLocaleDefault() {
        Locale result = OrderInternationalization.getLocale(999);
        assertEquals(Locale.getDefault(), result);
    }

    @Test
    void testShowOrderInfoRuBYLocale() {
        Locale russianLocale = new Locale("ru", "BY");
        ResourceBundle bundle = ResourceBundle.getBundle("order", russianLocale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(russianLocale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, russianLocale);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        OrderInternationalization.showOrderInfo(bundle, currencyFormat, dateFormat);

        String output = outputStream.toString();
        assertTrue(output.contains("Заказ: #12345"));
        assertTrue(output.contains("Дата:"));
        assertTrue(output.contains("Цена:"));

        System.setOut(System.out);
    }

    @Test
    void testShowOrderInfoUSLocale() {
        Locale englishLocale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("order", englishLocale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(englishLocale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, englishLocale);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        OrderInternationalization.showOrderInfo(bundle, currencyFormat, dateFormat);

        String output = outputStream.toString();
        assertTrue(output.contains("Order: #12345"));
        assertTrue(output.contains("Date:"));
        assertTrue(output.contains("Price:"));

        System.setOut(System.out);
    }

    @Test
    void testShowOrderInfoGermany() {
        Locale germanLocale = Locale.GERMANY;
        ResourceBundle bundle = ResourceBundle.getBundle("order", germanLocale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(germanLocale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, germanLocale);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        OrderInternationalization.showOrderInfo(bundle, currencyFormat, dateFormat);

        String output = outputStream.toString();
        assertTrue(output.contains("Bestellung: #12345"));
        assertTrue(output.contains("Datum:"));
        assertTrue(output.contains("Preis:"));

        System.setOut(System.out);
    }

    @Test
    void testResourceBundleContainsAllKeysRuBY() {
        Locale locale = new Locale("ru", "BY");
        ResourceBundle bundle = ResourceBundle.getBundle("order", locale);

        assertTrue(bundle.containsKey("order"));
        assertTrue(bundle.containsKey("date"));
        assertTrue(bundle.containsKey("price"));
        assertEquals("Заказ", bundle.getString("order"));
        assertEquals("Дата", bundle.getString("date"));
        assertEquals("Цена", bundle.getString("price"));
    }

    @Test
    void testResourceBundleContainsAllKeysUS() {
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("order", locale);

        assertTrue(bundle.containsKey("order"));
        assertTrue(bundle.containsKey("date"));
        assertTrue(bundle.containsKey("price"));
        assertEquals("Order", bundle.getString("order"));
        assertEquals("Date", bundle.getString("date"));
        assertEquals("Price", bundle.getString("price"));
    }

    @Test
    void testResourceBundleContainsAllKeysGerman() {
        Locale locale = Locale.GERMANY;
        ResourceBundle bundle = ResourceBundle.getBundle("order", locale);

        assertTrue(bundle.containsKey("order"));
        assertTrue(bundle.containsKey("date"));
        assertTrue(bundle.containsKey("price"));
        assertEquals("Bestellung", bundle.getString("order"));
        assertEquals("Datum", bundle.getString("date"));
        assertEquals("Preis", bundle.getString("price"));
    }

    @Test
    void testShowOrderInfoOutputStructure() {
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("order", locale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        OrderInternationalization.showOrderInfo(bundle, currencyFormat, dateFormat);

        String output = outputStream.toString();
        String[] lines = output.trim().split("\n");

        assertEquals(3, lines.length);
        assertTrue(lines[0].startsWith("Order: #12345"));
        assertTrue(lines[1].startsWith("Date:"));
        assertTrue(lines[2].startsWith("Price:"));

        System.setOut(System.out);
    }

    @Test
    void testShowOrderInfoUSPriceFormat() {
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("order", locale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        OrderInternationalization.showOrderInfo(bundle, currencyFormat, dateFormat);

        String output = outputStream.toString();
        assertTrue(output.contains("$999.99"));

        System.setOut(System.out);
    }

    @Test
    void testShowOrderInfoRuBYPriceFormat() {
        Locale locale = new Locale("ru", "BY");
        ResourceBundle bundle = ResourceBundle.getBundle("order", locale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        OrderInternationalization.showOrderInfo(bundle, currencyFormat, dateFormat);

        String output = outputStream.toString();
        assertTrue(output.contains("Br"));

        System.setOut(System.out);
    }

}