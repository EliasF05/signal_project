package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.HealthDataSimulator;
import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.FileOutputStrategy;
import com.cardio_generator.outputs.TcpOutputStrategy;
import com.cardio_generator.outputs.WebSocketOutputStrategy;

import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HealthDataSimulatorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private HealthDataSimulator simulator;
    private ScheduledExecutorService scheduler;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        HealthDataSimulator.reset(); // Ensure singleton is reset before each test
        simulator = HealthDataSimulator.getInstance();
        scheduler = mock(ScheduledExecutorService.class);
        simulator.setScheduler(scheduler);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testSingletonInstance() {
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance();
        assertSame(instance1, instance2, "Both instances should be the same");
    }

    @Test
    void testSetScheduler() {
        ScheduledExecutorService newScheduler = Executors.newScheduledThreadPool(4);
        simulator.setScheduler(newScheduler);
        assertSame(newScheduler, simulator.getScheduler(), "Scheduler should be set correctly");
    }

    @Test
    void testParseArgumentsWithPatientCount() throws IOException {
        String[] args = {"--patient-count", "100"};
        simulator.parseArguments(args);
        assertEquals(100, simulator.getPatientCount(), "Patient count should be set to 100");
    }

    @Test
    void testParseArgumentsWithInvalidPatientCount() throws IOException {
        String[] args = {"--patient-count", "invalid"};
        simulator.parseArguments(args);
        assertEquals(50, simulator.getPatientCount(), "Patient count should default to 50 on invalid input");
    }

    @Test
    void testParseArgumentsWithConsoleOutput() throws IOException {
        String[] args = {"--output", "console"};
        simulator.parseArguments(args);
        assertTrue(simulator.getOutputStrategy() instanceof ConsoleOutputStrategy, "Output strategy should be console");
    }

    @Test
    void testParseArgumentsWithFileOutput() throws IOException {
        String[] args = {"--output", "file:./test_output"};
        simulator.parseArguments(args);
        assertTrue(simulator.getOutputStrategy() instanceof FileOutputStrategy, "Output strategy should be file");
        assertTrue(Files.exists(Paths.get("./test_output")), "Output directory should be created");
        Files.delete(Paths.get("./test_output")); // Cleanup
    }

    @Test
    void testParseArgumentsWithWebSocketOutput() throws IOException {
        String[] args = {"--output", "websocket:8080"};
        simulator.parseArguments(args);
        assertTrue(simulator.getOutputStrategy() instanceof WebSocketOutputStrategy, "Output strategy should be websocket");
    }

    @Test
    void testParseArgumentsWithTcpOutput() throws IOException {
        String[] args = {"--output", "tcp:8080"};
        simulator.parseArguments(args);
        assertTrue(simulator.getOutputStrategy() instanceof TcpOutputStrategy, "Output strategy should be tcp");
    }

    @Test
    void testParseArgumentsWithInvalidOutput() throws IOException {
        String[] args = {"--output", "invalid"};
        simulator.parseArguments(args);
        assertTrue(simulator.getOutputStrategy() instanceof ConsoleOutputStrategy, "Default output strategy should be console");
    }

    @Test
    void testInitializePatientIds() {
        List<Integer> patientIds = simulator.initializePatientIds(5);
        assertEquals(5, patientIds.size(), "Should initialize 5 patient IDs");
        assertEquals(List.of(1, 2, 3, 4, 5), patientIds, "Patient IDs should be 1 to 5");
    }

  

}
