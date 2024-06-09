package data_management;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.data_management.DataStorage;
import com.data_management.SocketClient;
import com.data_management.WebSocketReader;

public class WebSocketTest {
    @Mock
    private DataStorage dataStorage;

    @Mock
    private SocketClient socketClient;

    @InjectMocks
    private WebSocketReader webSocketReader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    public void testConnect(){
        String uri = "ws://localhost:8080";
        webSocketReader = new WebSocketReader() {
            @Override
            public void connect(String uri) {
                try {
                    socketClient = mock(SocketClient.class);
                    socketClient.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        webSocketReader.connect(uri);

        verify(socketClient, times(1)).connect();
    }

    @Test
    public void testDisconnect(){
        webSocketReader = new WebSocketReader() {
            @Override
            public void disconnect() {
                try {
                    socketClient.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        webSocketReader.connect("ws://localhost:8080");

        webSocketReader.disconnect();

        verify(socketClient, times(1)).disconnect();
    }

    @Test 
    public void dataProcessingThroughWebSocket(){
        String uri = "ws://localhost:8080";
        // TODO (finish test)
    }
}
