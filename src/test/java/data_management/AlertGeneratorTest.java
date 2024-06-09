package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.AlertGenerator;

/**
 * Test cases for AlertGenerator class.
 */
class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {
        dataStorage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(dataStorage);
        DataStorage.reset(); // Ensure data storage is reset before each test
    }

    @Test
    void testNoAlertTriggered() {
        Patient patient = new Patient(1);
        patient.addRecord(120, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(80, "DiastolicPressure", System.currentTimeMillis());
        patient.addRecord(98, "Saturation", System.currentTimeMillis());
        patient.addRecord(70, "HeartRate", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        assertTrue(dataStorage.getRecords(1, 0, System.currentTimeMillis()).isEmpty(), 
                   "No alerts should be triggered for normal values");
    }

    @Test
    void testBloodPressureAlertTriggered() {
        Patient patient = new Patient(2);
        patient.addRecord(180, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(120, "DiastolicPressure", System.currentTimeMillis());
        patient.addRecord(98, "Saturation", System.currentTimeMillis());
        patient.addRecord(70, "ECG", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<PatientRecord> records = dataStorage.getRecords(2, 0, System.currentTimeMillis()+1);
        assertEquals(1, records.size(), "One alert should be triggered for high blood pressure");
        assertEquals("Abnormal blood pressure patterns detected!", records.get(0).getRecordType());
    }

    @Test
    void testBloodOxygenAlertTriggered() {
        Patient patient = new Patient(3);
        patient.addRecord(120, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(80, "DiastolicPressure", System.currentTimeMillis());
        patient.addRecord(85, "Saturation", System.currentTimeMillis());
        patient.addRecord(70, "ECG", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<PatientRecord> records = dataStorage.getRecords(3, 0, System.currentTimeMillis()+1);
        assertEquals(1, records.size(), "One alert should be triggered for low blood oxygen");
        assertEquals("Abnormal blood saturation patterns detected", records.get(0).getRecordType());
    }

    @Test
    void testHeartRateAlertTriggered() {
        Patient patient = new Patient(4);
        patient.addRecord(120, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(80, "DiastolicPressure", System.currentTimeMillis());
        patient.addRecord(98, "Saturation", System.currentTimeMillis());
        patient.addRecord(150, "ECG", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<PatientRecord> records = dataStorage.getRecords(4, 0, System.currentTimeMillis()+1);
        assertEquals(1, records.size(), "One alert should be triggered for abnormal heart rate");
        assertEquals("Abnormal ECG patterns detected", records.get(0).getRecordType());
    }

    @Test
    void testMultipleAlertsTriggered() {
        Patient patient = new Patient(5);
        patient.addRecord(180, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(120, "DiastolicPressure", System.currentTimeMillis());
        patient.addRecord(85, "Saturation", System.currentTimeMillis());
        patient.addRecord(150, "ECG", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<PatientRecord> records = dataStorage.getRecords(5, 0, System.currentTimeMillis()+1);
        assertEquals(3, records.size(), "Three alerts should be triggered for multiple abnormal values");
    }

    @Test
    void testEdgeCaseLowBloodPressure() {
        Patient patient = new Patient(6);
        patient.addRecord(90, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(60, "DiastolicPressure", System.currentTimeMillis());
        patient.addRecord(98, "Saturation", System.currentTimeMillis());
        patient.addRecord(70, "HeartRate", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<PatientRecord> records = dataStorage.getRecords(6, 0, System.currentTimeMillis());
        assertTrue(records.isEmpty(), "No alerts should be triggered for low but not critical blood pressure");
    }

    @Test
    void testEdgeCaseHighHeartRate() {
        Patient patient = new Patient(7);
        patient.addRecord(120, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(80, "DiastolicPressure", System.currentTimeMillis());
        patient.addRecord(98, "Saturation", System.currentTimeMillis());
        patient.addRecord(100, "HeartRate", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<PatientRecord> records = dataStorage.getRecords(7, 0, System.currentTimeMillis());
        assertTrue(records.isEmpty(), "No alerts should be triggered for high but not critical heart rate");
    }

    @Test
    void testBloodPressureTrendAlertTriggered() {
        Patient patient = new Patient(8);
        long currentTime = System.currentTimeMillis();
        
        // Add systolic pressure readings with increasing trend
        patient.addRecord(120, "SystolicPressure", currentTime - 3000);
        patient.addRecord(130, "SystolicPressure", currentTime - 2000);
        patient.addRecord(145, "SystolicPressure", currentTime - 1000);
        
        // Add diastolic pressure readings with increasing trend
        patient.addRecord(80, "DiastolicPressure", currentTime - 3000);
        patient.addRecord(90, "DiastolicPressure", currentTime - 2000);
        patient.addRecord(100, "DiastolicPressure", currentTime - 1000);

        alertGenerator.evaluateData(patient);

        List<PatientRecord> records = dataStorage.getRecords(8, 0, System.currentTimeMillis()+1);
        assertEquals(1, records.size(), "One alert should be triggered for blood pressure trend");
        assertEquals("Abnormal blood pressure patterns detected!", records.get(0).getRecordType());
    }
}
