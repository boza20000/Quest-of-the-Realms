package com.questoftherealm.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerLoggerTest {

    @Test
    void testLoggerWritesToFile() throws IOException {
        String testMessage = "Test log message " + System.currentTimeMillis();
        ServerLogger logger = ServerLogger.get();

        logger.info(testMessage);

        Path logPath = Path.of("log.txt");
        assertTrue(Files.exists(logPath), "Log file should exist");

        List<String> lines = Files.readAllLines(logPath);
        boolean found = false;
        for (String line : lines) {
            if (line.contains(testMessage) && line.contains("INFO")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Log file should contain the logged message with correct level");
    }

    @Test
    void testLoggerErrorFormat() throws IOException {
        String errorMessage = "Test error " + System.currentTimeMillis();
        ServerLogger logger = ServerLogger.get();

        logger.error(errorMessage);

        Path logPath = Path.of("log.txt");
        List<String> lines = Files.readAllLines(logPath);

        boolean found = false;
        for (String line : lines) {
            if (line.contains(errorMessage) && line.contains("ERROR")) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Log file should contain ERROR level and message");
    }
}
