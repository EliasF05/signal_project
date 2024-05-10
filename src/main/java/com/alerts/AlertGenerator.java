package com.alerts;

import java.util.ArrayList;
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
        PatientRecord lastSyst = patient.getLast("SystolicPressure");
        if (lastSyst!=null){
            if (lastSyst.getMeasurementValue()>180){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Threshold Alert: Systolic Pressure>180",  lastSyst.getTimestamp()));
            }
            else if (lastSyst.getMeasurementValue()<90){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Threshold Alert: Systolic Pressure<90",  lastSyst.getTimestamp()));
            }
        }

        PatientRecord lastDiast = patient.getLast("DiastolicPressure");
        if (lastDiast!=null){
            if (lastDiast.getMeasurementValue()>120){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Threshold Alert: Diastolic Pressure>120",  lastSyst.getTimestamp()));
            }
            else if (lastDiast.getMeasurementValue()<60){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Critical Threshold Alert: Diastolic Pressure<60",  lastSyst.getTimestamp()));
            }
        }
        // Blood Saturation thresholds
        PatientRecord lastSat = patient.getLast("Saturation");
        if (lastSat!=null&&lastSat.getMeasurementValue()<92){
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Low Saturation Alert: Blood Saturation<92", lastSat.getTimestamp()));
        }

        // Hypotensive Hypoxemia thresholds
        if (lastSyst!=null&&lastSat!=null&&lastSyst.getMeasurementValue()<90&&lastSat.getMeasurementValue()<92){
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Hypotensive Hypoxemia Alert", lastSat.getTimestamp()));
        }

        // ECG thresholds
        PatientRecord lastECG = patient.getLast("ECG");
        if (lastECG!=null){
            if (lastECG.getMeasurementValue()<50){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Abnormal Heart Rate Alert: BPM<50", lastECG.getTimestamp()));
            }
            else if (lastECG.getMeasurementValue()>100){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Abnormal Heart Rate Alert: BPM>100", lastECG.getTimestamp()));
            }
        }

        // Check for time-wise developements
        
        // find last 3 consecutive bloodPressure readings
        List<PatientRecord> systolicReadings = patient.getDiastolicReadings();
        List<PatientRecord> diastolicReadings = patient.getSystolicReadings();
        
        // Trigger for Increase/Decrease in blood pressure
        if (systolicReadings.size()==3&&diastolicReadings.size()==3){
            if (systolicReadings.get(2).getMeasurementValue()>systolicReadings.get(1).getMeasurementValue()+10&&systolicReadings.get(1).getMeasurementValue()>systolicReadings.get(0).getMeasurementValue()+10){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Systolic Pressure Increase Trend Alert", systolicReadings.get(2).getTimestamp()));
            }
            if (diastolicReadings.get(2).getMeasurementValue()>diastolicReadings.get(1).getMeasurementValue()+10&&diastolicReadings.get(1).getMeasurementValue()>diastolicReadings.get(0).getMeasurementValue()+10){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Diastolic Pressure Increase Trend Alert", diastolicReadings.get(2).getTimestamp()));
            }
            if (systolicReadings.get(2).getMeasurementValue()<systolicReadings.get(1).getMeasurementValue()-10&&systolicReadings.get(1).getMeasurementValue()<systolicReadings.get(0).getMeasurementValue()-10){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Systolic Pressure Decrease Trend Alert", systolicReadings.get(2).getTimestamp()));
            }
            if (diastolicReadings.get(2).getMeasurementValue()<diastolicReadings.get(1).getMeasurementValue()-10&&diastolicReadings.get(1).getMeasurementValue()<diastolicReadings.get(0).getMeasurementValue()-10){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Diastolic Pressure Decrease Trend Alert", diastolicReadings.get(2).getTimestamp()));
            }
        }

        // Trigger for rapid drop in blood saturation
        // Find blood saturation level since 10 mins ago
        List<PatientRecord> lastTenMins = patient.getRecords(System.currentTimeMillis()-600000, System.currentTimeMillis());
        for (PatientRecord record: lastTenMins){
            // Check for 5% decrease
            if (record.getRecordType()=="Saturation"){
                if (lastSat.getMeasurementValue()<record.getMeasurementValue()-record.getMeasurementValue()/20){
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Rapid Drop Alert in Blood Saturation", lastSat.getTimestamp()));
                }
                break;
            }  
        }
       
        // Trigger for irregular heart beat
        // Find last 3 ECG readings, check if intervals vary by more than 20%
        List<PatientRecord> lastThreeECGs = patient.getECGReadings().subList(patient.getECGReadings().size()-4, patient.getECGReadings().size()-1);
        if (Math.abs((lastThreeECGs.get(1).getMeasurementValue()-lastThreeECGs.get(0).getMeasurementValue())-(lastThreeECGs.get(2).getMeasurementValue()-lastThreeECGs.get(1).getMeasurementValue()))>lastThreeECGs.get(2).getMeasurementValue()*0.20){
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Irregular Beat Patterns Detected", lastThreeECGs.get(2).getTimestamp()));
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
