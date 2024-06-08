package com.alerts;

public class AlertFactory {
    public Alert createAlert(String patientId, String condition, long timestamp){
        if (condition.contains("Systolic Pressure")||condition.contains("Diastolic Pressure")){
            return new BloodPressureAlertFactory().createAlert(patientId, condition, timestamp);
        }
        if (condition.contains("Saturation")||condition.contains("Hypoxemia")){
            return new BloodOxgenAlertFactory().createAlert(patientId, condition, timestamp);
        }
        else{
            return new ECGAlertFactory().createAlert(patientId, condition, timestamp);
        }
    }
}
