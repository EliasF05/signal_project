package com.alerts;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy{

    @Override
    /**
     * Evaluates the specified patient's data to determine if any blood-pressure related alert conditions are met
     */
    public boolean checkAlert(Patient patient){
        List<PatientRecord> systPressure = patient.getSystolicReadings();
        int systLength = systPressure.size();
        if (systLength>0){
            PatientRecord lastSystReading = systPressure.get(systLength-1);
            if (lastSystReading.getMeasurementValue()>180||lastSystReading.getMeasurementValue()<90){
                return true;
            }
        }
        List<PatientRecord> diastPressure = patient.getDiastolicReadings();
        int diastLength = diastPressure.size();
        if (diastLength>0){
            PatientRecord diastRecord = diastPressure.get(diastLength-1);
            if (diastRecord.getMeasurementValue()>120||diastRecord.getMeasurementValue()<60){
                return true;
            }
        }

        if (systLength>=3&&diastLength>=3){
            if(systPressure.get(systLength-1).getMeasurementValue()>systPressure.get(diastLength-2).getMeasurementValue()+10
                &&systPressure.get(diastLength-2).getMeasurementValue()>systPressure.get(diastLength-3).getMeasurementValue()+10){
                    return true;
            }

            if(systPressure.get(systLength-1).getMeasurementValue()>systPressure.get(diastLength-2).getMeasurementValue()-10
                &&systPressure.get(diastLength-2).getMeasurementValue()>systPressure.get(diastLength-3).getMeasurementValue()-10){
                return true;
            }


            if(diastPressure.get(systLength-1).getMeasurementValue()>diastPressure.get(diastLength-2).getMeasurementValue()+10
                &&diastPressure.get(diastLength-2).getMeasurementValue()>diastPressure.get(diastLength-3).getMeasurementValue()+10){
                return true;
            }

            if(diastPressure.get(systLength-1).getMeasurementValue()>diastPressure.get(diastLength-2).getMeasurementValue()-10
                &&diastPressure.get(diastLength-2).getMeasurementValue()>diastPressure.get(diastLength-3).getMeasurementValue()-10){
                return true;
            }
        }
        return false;
    }
}
