package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.AlertDecorator;
import com.alerts.PriorityAlertDecorator;
import com.alerts.RepeatedAlertDecorator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlertDecoratorTest {

    private Alert mockAlert;
    private Alert repeatedAlertDecorator;
    private Alert priorityAlertDecorator;

    @BeforeEach
    void setUp() {
        mockAlert = mock(Alert.class);
        when(mockAlert.getPatientId()).thenReturn("12345");
        when(mockAlert.getCondition()).thenReturn("High Blood Pressure");
        when(mockAlert.getTimestamp()).thenReturn(1627842938000L);

        repeatedAlertDecorator = new RepeatedAlertDecorator(mockAlert, 30);
        priorityAlertDecorator = new PriorityAlertDecorator(mockAlert, "High");
    }

    @Test
    void testAlertDecorator() {
        AlertDecorator alertDecorator = new AlertDecorator(mockAlert) {};

        assertEquals("12345", alertDecorator.getPatientId());
        assertEquals("High Blood Pressure", alertDecorator.getCondition());
        assertEquals(1627842938000L, alertDecorator.getTimestamp());
    }

    @Test
    void testRepeatedAlertDecorator() {
        assertEquals("12345", repeatedAlertDecorator.getPatientId());
        assertEquals("High Blood Pressure", repeatedAlertDecorator.getCondition());
        assertEquals(1627842938000L, repeatedAlertDecorator.getTimestamp());

        // Simulating the check and repeat action
        ((RepeatedAlertDecorator) repeatedAlertDecorator).checkAndRepeat();
        // Since checkAndRepeat() only prints to console, we can manually check the console output
    }

    @Test
    void testPriorityAlertDecorator() {
        assertEquals("12345", priorityAlertDecorator.getPatientId());
        assertEquals("High Blood Pressure (Priority: High)", priorityAlertDecorator.getCondition());
        assertEquals(1627842938000L, priorityAlertDecorator.getTimestamp());

        assertEquals("High", ((PriorityAlertDecorator) priorityAlertDecorator).getPriorityLevel());
    }

    @Test
    void testCombinedDecorators() {
        Alert combinedDecorator = new PriorityAlertDecorator(
                new RepeatedAlertDecorator(mockAlert, 30), "High");

        assertEquals("12345", combinedDecorator.getPatientId());
        assertEquals("High Blood Pressure (Priority: High)", combinedDecorator.getCondition());
        assertEquals(1627842938000L, combinedDecorator.getTimestamp());

        ((RepeatedAlertDecorator) ((PriorityAlertDecorator) combinedDecorator).getDecoratedAlert()).checkAndRepeat();
        assertEquals("High", ((PriorityAlertDecorator) combinedDecorator).getPriorityLevel());
    }
}
