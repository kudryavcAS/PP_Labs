package org.decorator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FireAlertDecoratorTest {

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void basicSMSAlert() {
        FireAlert alert = new BasicSMSAlert();
        String testMessage = "FIRE!";

        alert.sendAlert(testMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("SMS alert:\n" + testMessage));
    }

    @Test
    void emailAlertDecorator() {
        FireAlert basicAlert = new BasicSMSAlert();
        FireAlert emailAlert = new EmailAlertDecorator(basicAlert);
        String testMessage = "FIRE!!!!";

        emailAlert.sendAlert(testMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("SMS alert:\n" + testMessage));
        assertTrue(output.contains("Email alert:\n" + testMessage));
    }

    @Test
    void telegramAlertDecorator() {
        FireAlert basicAlert = new BasicSMSAlert();
        FireAlert telegramAlert = new TelegramAlertDecorator(basicAlert);
        String testMessage = "FIRE!!";

        telegramAlert.sendAlert(testMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("SMS alert:\n" + testMessage));
        assertTrue(output.contains("Telegram alert:\n" + testMessage));
    }

    @Test
    void telegramEmailAlertDecorator() {
        FireAlert basicAlert = new BasicSMSAlert();
        FireAlert telegramAlert = new TelegramAlertDecorator(basicAlert);
        FireAlert emailAlert = new EmailAlertDecorator(telegramAlert);

        String testMessage = "FIRE!!";
        emailAlert.sendAlert(testMessage);

        String output = outputStream.toString();
        assertTrue(output.contains("SMS alert:\n" + testMessage));
        assertTrue(output.contains("Telegram alert:\n" + testMessage));
        assertTrue(output.contains("Email alert:\n" + testMessage));

    }

}