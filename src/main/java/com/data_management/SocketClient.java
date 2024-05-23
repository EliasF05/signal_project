package com.data_management;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class SocketClient extends WebSocketClient implements DataReader{
    
        private DataStorage dataStorage;

        public SocketClient(DataStorage dataStorage, URI serverUri){
            super(serverUri);
            this.dataStorage = dataStorage;
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake){
            System.out.println("Connected to WebSocket server");
        }

        @Override
        public void onMessage(String message){
            // Handle data in same way as in OutReader
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

        @Override
        public void onClose(int code, String reason, boolean remote){
            System.out.println("Connection closed: "+reason);
        }

        @Override
        public void onError(Exception e){
            System.err.println("Error occured:");
            e.printStackTrace();
        }

        @Override
        public void connect(String uri){
            try{
                this.connectBlocking();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        @Override
        public void disconnect(){
            this.close();
        }
}
