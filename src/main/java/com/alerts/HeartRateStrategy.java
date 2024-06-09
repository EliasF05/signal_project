package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy{
    @Override
    public boolean checkAlert(Patient patient){
        if (patient.getECGReadings().size()<1){
            return false;
        }
        PatientRecord data = patient.getECGReadings().get(patient.getECGReadings().size()-1);
        if(data.getRecordType().equals("ECG")){
            if (data.getMeasurementValue()<50){
                return true;
            }
            if (data.getMeasurementValue()>100){
                return true;
            }
        }
        return false;
    }
}
