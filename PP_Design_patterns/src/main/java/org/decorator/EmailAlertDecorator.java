package org.decorator;

public class EmailAlertDecorator extends AlertDecorator {
    public EmailAlertDecorator(FireAlert decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public void sendAlert(String message) {
        super.sendAlert(message);
        sendEmail(message);
    }

    private void sendEmail(String message) {
        System.out.println("Email alert:\n" + message);
    }
}
