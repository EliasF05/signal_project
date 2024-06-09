package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatInterval; // in seconds

    public RepeatedAlertDecorator(Alert decoratedAlert, int repeatInterval) {
        super(decoratedAlert);
        this.repeatInterval = repeatInterval;
    }

    public void checkAndRepeat() {
        // Logic to check and repeat the alert based on the repeatInterval
        System.out.println("Checking and repeating alert every " + repeatInterval + " seconds for patient " + getPatientId());
    }
}
