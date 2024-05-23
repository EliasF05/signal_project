package com.data_management;

import java.net.URISyntaxException;

public class WebSocketReader implements DataReader{

    public void readData(DataStorage dataStorage){ 
        try {
            SocketClient client = new SocketClient(dataStorage, "ws://localhost:"+8080);
            client.connect();
        } catch (URISyntaxException e) {
            System.err.println("Unable to initialize client. Check Syntax of specified URI.");
            e.printStackTrace();
        }

    }
}
