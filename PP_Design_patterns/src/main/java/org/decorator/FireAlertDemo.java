package org.decorator;

public class FireAlertDemo {
    public static void main(String[] args) {
        String fireMessage = "Fire! Your house is on fire. And there's nothing you can do about it, lol";

        System.out.println("1. Only SMS:");
        FireAlert basicSMS = new BasicSMSAlert();
        basicSMS.sendAlert(fireMessage);

        System.out.println("\n2. SMS + Email");
        FireAlert doubleAlert = new EmailAlertDecorator(new BasicSMSAlert());
        doubleAlert.sendAlert(fireMessage);

        System.out.println("\n3. SMS + Email + Telegram");
        FireAlert tripleAlert1 = new TelegramAlertDecorator(new EmailAlertDecorator(new BasicSMSAlert()));
        tripleAlert1.sendAlert(fireMessage);

        System.out.println("\n4. SMS + Telegram + Email");
        FireAlert tripleAlert2 = new EmailAlertDecorator(new TelegramAlertDecorator(new BasicSMSAlert()));
        tripleAlert2.sendAlert(fireMessage);

    }
}
