/**
 * Defines functionality for data generators for health monitoring simulations.
 */
package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

public interface PatientDataGenerator {
    /**
     * bluepring for generating health data
     * 
     * @param patientId Id of Patient in question
     * @param outputStrategy Way of outputting the simulated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
