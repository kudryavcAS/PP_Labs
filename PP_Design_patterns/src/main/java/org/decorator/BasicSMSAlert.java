package org.decorator;

public class BasicSMSAlert implements FireAlert {

    @Override
    public void sendAlert(String message) {
        sendSMS(message);
    }

    private void sendSMS(String message) {
        System.out.println("Basic SMS alert:\n" + message);
    }
}
