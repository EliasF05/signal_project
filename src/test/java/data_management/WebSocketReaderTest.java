package data_management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.data_management.DataStorage;
import com.data_management.SocketClient;
import com.data_management.WebSocketReader;

import java.net.URI;
import java.net.URISyntaxException;

class WebSocketReaderTest {

    private WebSocketReader webSocketReader;
    private DataStorage mockDataStorage;
    private SocketClient mockClient;

    @BeforeEach
    void setUp() {
        mockDataStorage = mock(DataStorage.class);
        webSocketReader = new WebSocketReader();
        webSocketReader.readData(mockDataStorage);
    }

    @Test
    void testDisconnectWhenNotConnected() {
        webSocketReader.disconnect();
        assertNull(webSocketReader.client);
    }

    @Test
    void testOnMessage() {
        String message = "Patient ID: 01, Timestamp: 1714376789050, Label: WhiteBloodCells, Data: 100.0";

        webSocketReader.onMessage(message);

        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Double> measurementValueCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<String> recordTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timestampCaptor = ArgumentCaptor.forClass(Long.class);

        verify(mockDataStorage).addPatientData(patientIdCaptor.capture(), measurementValueCaptor.capture(),
                recordTypeCaptor.capture(), timestampCaptor.capture());

        assertEquals(1, patientIdCaptor.getValue());
        assertEquals(100.0, measurementValueCaptor.getValue());
        assertEquals(" WhiteBloodCells", recordTypeCaptor.getValue());
        assertEquals(1714376789050L, timestampCaptor.getValue());
    }

    @Test
    void testOnMessageWithSaturation() {
        String message = "Patient ID: 01, Timestamp: 1714376789050, Label: Saturation, Data: 95.5";

        webSocketReader.onMessage(message);

        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Double> measurementValueCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<String> recordTypeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timestampCaptor = ArgumentCaptor.forClass(Long.class);

        verify(mockDataStorage).addPatientData(patientIdCaptor.capture(), measurementValueCaptor.capture(),
                recordTypeCaptor.capture(), timestampCaptor.capture());

        assertEquals(1, patientIdCaptor.getValue());
        assertEquals(95.5, measurementValueCaptor.getValue());
        assertEquals(" Saturation", recordTypeCaptor.getValue());
        assertEquals(1714376789050L, timestampCaptor.getValue());
    }
}
