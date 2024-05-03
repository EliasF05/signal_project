package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

public class OutReader implements DataReader{
    
    private String outputDir;

    public OutReader(String outputDir){
        this.outputDir =outputDir;
    }
    public void readData(DataStorage dataStorage) throws IOException{
        try {
            File file = new File(outputDir);
            Reader reader = new java.io.FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine())!=null){
                String[] data = line.split(",");
                
                int parientId = Integer.parseInt(data[0]);
                // TODO change measugohsfijae
                double measurementValue = Double.parseDouble(data[1]);
                String recordType = data[2];
                Long timestamp = Long.parseLong(data[3]);
                dataStorage.addPatientData(parientId, measurementValue, recordType, timestamp);
            }
            bufferedReader.close();

        } catch (Exception e) {
            throw new IOException("Error reading data from file: "+outputDir, e);
        }
    }
}
