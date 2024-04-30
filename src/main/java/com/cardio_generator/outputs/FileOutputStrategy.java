/**
 * Represents a writer for health monitoring simulations
 * This class is responsible to writing simulated data to specified files
 * It utilizes Java IO and nio Libraries
 */
package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

// Changing class name to UpperCamelCase
public class FileOutputStrategy implements OutputStrategy {
    // non-constant field to lower Camel Case
    private String baseDirectory;
    // final instance fields to lower Camel Case
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Initializes fileOutputStrategy
     * 
     * @param baseDirectory baseDirectory to write data to
     */
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes health data to file in Base Directory
     * 
     * @param patientId Patient the data was generated for
     * @param timestamp point in time data was generated for
     * @param label Type of data simulated
     * @param data Simulated data
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
            // print stack trace
            e.printStackTrace();
        }
    }
}