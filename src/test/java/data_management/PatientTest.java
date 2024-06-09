package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

class PatientTest {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(1);
    }

    @Test
    void testAddRecord() {
        patient.addRecord(120, "SystolicPressure", System.currentTimeMillis());
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis() + 1);
        assertEquals(1, records.size());
    }

    @Test
    void testGetRecords() {
        long currentTime = System.currentTimeMillis();
        patient.addRecord(120, "SystolicPressure", currentTime);
        List<PatientRecord> records = patient.getRecords(currentTime - 1, currentTime + 1);
        assertEquals(1, records.size());
    }

    @Test
    void testGetLastRecord() {
        patient.addRecord(120, "SystolicPressure", System.currentTimeMillis());
        PatientRecord lastRecord = patient.getLast("SystolicPressure");
        assertNotNull(lastRecord);
    }
}
