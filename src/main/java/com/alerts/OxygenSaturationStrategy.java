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

        // We'll also check for Hypotensive Hypoxemia in here
        if (patient.getSystolicReadings().size()<1){
            return false;
        }
        PatientRecord systData = patient.getSystolicReadings().get(patient.getSystolicReadings().size()-1);
        if (data.getMeasurementValue()<92&&systData.getMeasurementValue()<90){
            return true;
        }
        return false;
    }
}
