package org.decorator;

public abstract class AlertDecorator implements FireAlert {

    protected FireAlert decoratedAlert;

    public AlertDecorator(FireAlert decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }

    @Override
    public void sendAlert(String message) {
        decoratedAlert.sendAlert(message);
    }
}
