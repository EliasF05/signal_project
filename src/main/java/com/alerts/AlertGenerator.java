package com.alerts;

import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Implementation goes here
        // Check for all critical thresholds
        // Blood Pressure thresholds
        BloodPressureStrategy pressureStrats = new BloodPressureStrategy();
        if (pressureStrats.checkAlert(patient)){
            triggerAlert(new BloodPressureAlert(String.valueOf(patient.getPatientId()), "Abnormal blood pressure patterns detected!", System.currentTimeMillis()));
        }
        // Blood Saturation thresholds
        OxygenSaturationStrategy oxygenStrats = new OxygenSaturationStrategy();
        if (oxygenStrats.checkAlert(patient)){
            triggerAlert(new BloodOxygenAlert(String.valueOf(patient.getPatientId()), "Abnormal blood saturation patterns detected", System.currentTimeMillis()));
        }

        HeartRateStrategy ecgStrats = new HeartRateStrategy();
        if (ecgStrats.checkAlert(patient)){
            triggerAlert(new ECGAlert(String.valueOf(patient.getPatientId()), "Abnormal ECG patterns detected", System.currentTimeMillis()));
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff    
        dataStorage.addPatientData(Integer.parseInt(alert.getPatientId()), 0, alert.getCondition(), alert.getTimestamp());
    }
}
