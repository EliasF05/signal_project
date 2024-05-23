package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketReader implements DataReader{

    private String uri = "ws://localhost:"+8080;
    private DataStorage dataStorage;
    private SocketClient client;

    public void readData(DataStorage dataStorage){ 
        this.dataStorage = dataStorage;
    }

    public void connect(String uri){
        try {
            client = new SocketClient(dataStorage, new URI(uri));
            client.connect();
        } catch (URISyntaxException e) {
            System.err.println("Unable to initialize client. Check Syntax of specified URI.");
            e.printStackTrace();
        }
    }

    public void disconnect(){
        if(client!=null){
            try{
                client.disconnect();
                client = null;
            }
            catch(Exception e){
                System.out.println("Failed to disconnect client:");
                e.printStackTrace();
            }
        }
    }

    public void onMessage(String message){
        // parse data and log it 
        String[] data = message.split(",");
        int parientId = Integer.parseInt(data[0].substring(data[0].length()-2, data[0].length()));
        Long timestamp = Long.parseLong(data[1].substring(data[1].length()-13, data[1].length()));
        String recordType = data[2].substring(7, data[2].length());
        double measurementValue;
        if (recordType=="Saturation"){
            measurementValue = Double.parseDouble(data[3].substring(6, data[3].length()-1));
        }
        else{
            measurementValue = Double.parseDouble(data[3].substring(6, data[3].length()));
        }
        dataStorage.addPatientData(parientId, measurementValue, recordType, timestamp);
    }
}
