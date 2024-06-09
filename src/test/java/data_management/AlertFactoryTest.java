package data_management;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.AlertFactory;
import com.alerts.BloodOxygenAlert;
import com.alerts.BloodPressureAlert;
import com.alerts.ECGAlert;

import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    @Test
    void testCreateAlert() {
        AlertFactory alertFactory = new AlertFactory();

        // Test BloodPressureAlert creation
        Alert bloodPressureAlert = alertFactory.createAlert("123", "High Systolic Pressure", 1627842938000L);
        assertTrue(bloodPressureAlert instanceof BloodPressureAlert);
        assertEquals("123", bloodPressureAlert.getPatientId());
        assertEquals("High Systolic Pressure", bloodPressureAlert.getCondition());
        assertEquals(1627842938000L, bloodPressureAlert.getTimestamp());

        // Test BloodOxygenAlert creation
        Alert bloodOxygenAlert = alertFactory.createAlert("456", "Low Saturation", 1627842938000L);
        assertTrue(bloodOxygenAlert instanceof BloodOxygenAlert);
        assertEquals("456", bloodOxygenAlert.getPatientId());
        assertEquals("Low Saturation", bloodOxygenAlert.getCondition());
        assertEquals(1627842938000L, bloodOxygenAlert.getTimestamp());

        // Test ECGAlert creation
        Alert ecgAlert = alertFactory.createAlert("789", "Abnormal ECG patterns", 1627842938000L);
        assertTrue(ecgAlert instanceof ECGAlert);
        assertEquals("789", ecgAlert.getPatientId());
        assertEquals("Abnormal ECG patterns", ecgAlert.getCondition());
        assertEquals(1627842938000L, ecgAlert.getTimestamp());
    }
}
