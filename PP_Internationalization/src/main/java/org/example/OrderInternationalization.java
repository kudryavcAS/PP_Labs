package org.example;

import java.text.NumberFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class OrderInternationalization {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Choose language:");
        System.out.println("1 - Русский");
        System.out.println("2 - English");
        System.out.println("3 - Deutsch");

        int choice = in.nextInt();
        Locale locale = getLocale(choice);

        ResourceBundle orderMessages = ResourceBundle.getBundle("order", locale);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        showOrderInfo(orderMessages, currencyFormat, dateFormat);
    }

    public static Locale getLocale(int choice) {
        return switch (choice) {
            case 1 -> new Locale("ru", "BY");
            case 2 -> Locale.US;
            case 3 -> Locale.GERMANY;
            default -> Locale.getDefault();
        };
    }

    public static void showOrderInfo(ResourceBundle orderMessages,
                                      NumberFormat currencyFormat,
                                      DateFormat dateFormat) {

        System.out.println("\n" + orderMessages.getString("order") + ": #12345");
        System.out.println(orderMessages.getString("date") + ": " + dateFormat.format(new Date()));

        double price = 999.99;
        System.out.println(orderMessages.getString("price") + ": " + currencyFormat.format(price));
    }
}