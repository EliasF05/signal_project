package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketReader implements DataReader {

    private String uri;
    private DataStorage dataStorage;
    public SocketClient client;

    public void readData(DataStorage dataStorage) { 
        this.dataStorage = dataStorage;
    }

    public void connect(String uri) {
        try {
            this.uri = uri;
            client = new SocketClient(dataStorage, new URI(uri));
            client.connectBlocking();
        } catch (URISyntaxException e) {
            System.err.println("Unable to initialize client. Check Syntax of specified URI.");
            e.printStackTrace();
            throw new RuntimeException("Invalid URI syntax");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Connection interrupted");
        }
    }

    public void disconnect() {
        if (client != null) {
            try {
                client.close();
                client = null;
            } catch (Exception e) {
                System.out.println("Failed to disconnect client:");
                e.printStackTrace();
            }
        }
    }

    public void onMessage(String message) {
        // parse data and log it 
        String[] data = message.split(",");
        int patientId = Integer.parseInt(data[0].substring(data[0].length() - 2));
        long timestamp = Long.parseLong(data[1].substring(data[1].length() - 13));
        String recordType = data[2].substring(7);
        double measurementValue;
        if (recordType.equals("Saturation")) {
            measurementValue = Double.parseDouble(data[3].substring(6, data[3].length() - 1));
        } else {
            measurementValue = Double.parseDouble(data[3].substring(6));
        }
        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
    }
}
