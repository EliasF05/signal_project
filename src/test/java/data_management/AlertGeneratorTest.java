package data_management;

import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AlertGeneratorTest
 * 
 * General Logic behind these tests:
 * Testing how many things are logged as patient data in the storage, as alerts are also logged
 * So if there is a measurement that triggers an alert, for example an ECG<50, then the amount of Patient Data in the storage for 
 * this patient is:
 * originalMeasurement+triggerAlert = 2
 * This way we can confirm an alert has been triggered. We can be confident it is the right alarm, as all testcases pass 
 * for different alerts, and it is very easily verifable from the logic in AlertGenerator.evaluate(), that no wrong alarm can be triggered.
 */
public class AlertGeneratorTest {

    @Test
    void testBloodPressureAlerts(){
        DataStorage.reset();
        DataStorage storage = DataStorage.getInstance();
        // Simulate data that should trigger blood pressure alerts
      
        
        storage.addPatientData(2024, 190, "SystolicPressure", System.currentTimeMillis());
        storage.addPatientData(2025, 55, "DiastolicPressure", System.currentTimeMillis());
    
        AlertGenerator hopeThisWorks = new AlertGenerator(storage);
        hopeThisWorks.evaluateData(storage.getAllPatients().get(0));
        hopeThisWorks.evaluateData(storage.getAllPatients().get(1));
        
        // Check that alerts have been added to records as length exceeds one for each patient
        assertEquals(2, storage.getRecords(2024, 0, System.currentTimeMillis()+1).size());
        assertEquals(2, storage.getRecords(2025, 0, System.currentTimeMillis()+1).size());
    }

    @Test
    void testSaturationAlerts(){
        DataStorage.reset();
        DataStorage storage = DataStorage.getInstance();
        // Simulate data that should trigger blood saturation alerts
        storage.addPatientData(2024, 91, "Saturation", System.currentTimeMillis());
        storage.addPatientData(2025, 99, "Saturation", System.currentTimeMillis());
        storage.addPatientData(2025, 93, "Saturation", System.currentTimeMillis());

        AlertGenerator hopeThisWorksToo = new AlertGenerator(storage);
        hopeThisWorksToo.evaluateData(storage.getAllPatients().get(0));
        hopeThisWorksToo.evaluateData(storage.getAllPatients().get(1));
        // Check that alerts have been added to records 
        assertEquals(2, storage.getRecords(2024, 0, System.currentTimeMillis()+1).size());
        assertEquals(3, storage.getRecords(2025, 0, System.currentTimeMillis()+1).size());
    }

    @Test
    void testHypotensiveHypoxemiaAlerts(){
        DataStorage.reset();
        DataStorage storage = DataStorage.getInstance();
        // Simulate data that should trigger hypotensive hypoxemia alerts
        storage.addPatientData(2024, 89, "SystolicPressure", System.currentTimeMillis());
        storage.addPatientData(2024, 91, "Saturation", System.currentTimeMillis()); 

        AlertGenerator stillHoping = new AlertGenerator(storage);
        stillHoping.evaluateData(storage.getAllPatients().get(0));
        // Check that alerts have been added to records (Systolic too low, saturation too low and Hypoxemia, so 3+2=5 in this case)
        assertEquals(5, storage.getRecords(2024, 0, System.currentTimeMillis()+1).size());
    }

    @Test
    void testECGAlerts(){
        DataStorage.reset();
        DataStorage storage = DataStorage.getInstance();
        // Simulate data that should trigger ECG alerts
        storage.addPatientData(2028, 33, "ECG", System.currentTimeMillis());
        storage.addPatientData(2028, 180, "ECG", System.currentTimeMillis());

        AlertGenerator dontStopBelievin = new AlertGenerator(storage);
        dontStopBelievin.evaluateData(storage.getAllPatients().get(0));
        // Check that alerts have been added to records
        assertEquals(3, storage.getRecords(2028, 0, System.currentTimeMillis()+1).size());
    }
}