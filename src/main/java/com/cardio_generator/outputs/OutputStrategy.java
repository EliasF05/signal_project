/**
 * Defines functionality to output simulated health data
 */
package com.cardio_generator.outputs;

public interface OutputStrategy {
    /**
     * blueprint for generating health data
     *   
     * @param patientId Id of Patient in question
     * @param timestamp Point in time of simulated data
     * @param label Type of data simulated
     * @param data Simulated data
     */
    void output(int patientId, long timestamp, String label, String data);
}
