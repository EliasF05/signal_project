package data_management;

import com.cardio_generator.generators.BloodLevelsDataGenerator;
import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.generators.BloodSaturationDataGenerator;
import com.cardio_generator.generators.ECGDataGenerator;
import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PatientDataGeneratorTest {

    private OutputStrategy mockOutputStrategy;

    @BeforeEach
    void setUp() {
        mockOutputStrategy = mock(OutputStrategy.class);
    }

    @Test
    void testECGDataGenerator() {
        ECGDataGenerator ecgDataGenerator = new ECGDataGenerator(1);
        ecgDataGenerator.generate(1, mockOutputStrategy);

        verify(mockOutputStrategy, times(1)).output(eq(1), anyLong(), eq("ECG"), anyString());
    }

    @Test
    void testBloodSaturationDataGenerator() {
        BloodSaturationDataGenerator saturationDataGenerator = new BloodSaturationDataGenerator(1);
        saturationDataGenerator.generate(1, mockOutputStrategy);

        verify(mockOutputStrategy, times(1)).output(eq(1), anyLong(), eq("Saturation"), anyString());
    }

    @Test
    void testBloodPressureDataGenerator() {
        BloodPressureDataGenerator bloodPressureDataGenerator = new BloodPressureDataGenerator(1);
        bloodPressureDataGenerator.generate(1, mockOutputStrategy);

        verify(mockOutputStrategy, times(1)).output(eq(1), anyLong(), eq("SystolicPressure"), anyString());
        verify(mockOutputStrategy, times(1)).output(eq(1), anyLong(), eq("DiastolicPressure"), anyString());
    }

    @Test
    void testBloodLevelsDataGenerator() {
        BloodLevelsDataGenerator bloodLevelsDataGenerator = new BloodLevelsDataGenerator(1);
        bloodLevelsDataGenerator.generate(1, mockOutputStrategy);

        verify(mockOutputStrategy, times(1)).output(eq(1), anyLong(), eq("Cholesterol"), anyString());
        verify(mockOutputStrategy, times(1)).output(eq(1), anyLong(), eq("WhiteBloodCells"), anyString());
        verify(mockOutputStrategy, times(1)).output(eq(1), anyLong(), eq("RedBloodCells"), anyString());
    }
}
