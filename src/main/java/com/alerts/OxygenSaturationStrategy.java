package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class OxygenSaturationStrategy implements AlertStrategy{
    @Override
    public boolean checkAlert(Patient patient){
        if (patient.getSaturationReadings().size()<1){
            return false;
        }
        PatientRecord data = patient.getSaturationReadings().get(patient.getSaturationReadings().size()-1);
        if (data.getRecordType().equals("Saturation")){
            if (data.getMeasurementValue()<92){
                return true;
            }
        }
        return true;
    }
}
