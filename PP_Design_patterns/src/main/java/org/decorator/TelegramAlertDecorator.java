package org.decorator;

public class TelegramAlertDecorator extends AlertDecorator {
    public TelegramAlertDecorator(FireAlert decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public void sendAlert(String message) {
        super.sendAlert(message);
        sendTelegram(message);
    }

    private void sendTelegram(String message) {
        System.out.println("Telegram alert:\n" + message);
    }
}
