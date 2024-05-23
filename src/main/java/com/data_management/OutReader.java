package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

public class OutReader implements OldDataReader{
    
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
            bufferedReader.close();

        } catch (Exception e) {
            throw new IOException("Error reading data from file: "+outputDir, e);
        }
    }
}
