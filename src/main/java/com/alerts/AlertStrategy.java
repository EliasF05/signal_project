package com.alerts;

import com.data_management.Patient;

public interface AlertStrategy {
    public boolean checkAlert(Patient patient);
}
