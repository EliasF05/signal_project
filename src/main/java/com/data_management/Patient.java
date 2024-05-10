package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;
    // Extra Fields to make alert triggering more efficient
    private List<PatientRecord> systolicRecords;
    private List<PatientRecord> diastolicRecords;
    private List<PatientRecord> ECGRecords;

    // Maps record type to last record of this kind
    private HashMap<String, PatientRecord> lastRecord = new HashMap<>();
    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and
     * timestamp.
     *
     * @param measurementValue the measurement value to store in the record
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since UNIX epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
        if (recordType=="SystolicPressure"){
            systolicRecords.add(record);
        }
        if (recordType=="DiastolicPressure"){
            diastolicRecords.add(record);
        }
        if (recordType=="ECG"){
            ECGRecords.add(record);
        }
        lastRecord.put(recordType, record);
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within a
     * specified time range.
     * The method filters records based on the start and end times provided.
     *
     * @param startTime the start of the time range, in milliseconds since UNIX
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since UNIX epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        if (endTime>startTime){
            throw new IllegalArgumentException("Start time argument must be smaller than endTime argument when retrieving patient records.");
        }
        // binary search to find start point
        int left = 0;
        int right = patientRecords.size()-1;
        int start = -1;
        int end = -1;
        if (startTime<patientRecords.get(0).getTimestamp()){ start = 0;}
        else if(startTime>patientRecords.get(patientRecords.size()-1).getTimestamp()){return new ArrayList<>();}
        else{
            while (left<right){
                int m = (left+right)/2;
                if (patientRecords.get(m).getTimestamp()==startTime){
                    start = m;
                    break;
                }
                else if(patientRecords.get(m).getTimestamp()<startTime){
                    if(m+1<patientRecords.size()&&patientRecords.get(m+1).getTimestamp()>=startTime){
                        start = m+1;
                        break;
                    }
                    left = m+1;
                }
                else{
                    if(m-1>=0&&patientRecords.get(m-1).getTimestamp()<=startTime){
                        start = m-1;
                        break;
                    }
                    right = m-1;
                }
            }
        }
        
        //binary search to find end point
        if(endTime>patientRecords.get(patientRecords.size()-1).getTimestamp()){end = patientRecords.size()-1;}
        else{
            while (left<right){
                int m = (left+right)/2;
                if (patientRecords.get(m).getTimestamp()==endTime){
                    end = m;
                    break;
                }
                else if(patientRecords.get(m).getTimestamp()<startTime){
                    if(m+1<patientRecords.size()&&patientRecords.get(m+1).getTimestamp()>=endTime){
                        end = m+1;
                        break;
                    }
                    left = m+1;
                }
                else{
                    if(m-1>=0&&patientRecords.get(m-1).getTimestamp()<=endTime){
                        end = m-1;
                        break;
                    }
                    right = m-1;
                }
            }
        }
        return patientRecords.subList(start, end+1);
    }

    public List<PatientRecord> getSystolicReadings(){
        return systolicRecords;
    }

    public List<PatientRecord> getDiastolicReadings(){
        return diastolicRecords;
    }

    public List<PatientRecord> getECGReadings(){
        return ECGRecords;
    }

    public PatientRecord getLast(String recordType){
        if (lastRecord.containsKey(recordType)){
            return lastRecord.get(recordType);
        }
        return null;
    }


    /**
     * Retrieves patient ID of this patient object
     * @return Patient ID
     */
    public int getPatientId(){
        return patientId;
    }

    
}
