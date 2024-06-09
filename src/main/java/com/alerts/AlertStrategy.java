package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public interface AlertStrategy {
    public boolean checkAlert(Patient patient);
}
