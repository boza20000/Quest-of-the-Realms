package com.questoftherealm.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ServerIntegrationTest {

    private Thread serverThread;
    private static final int PORT = 2020;

    @BeforeEach
    void startServerThread() throws InterruptedException {
        serverThread = new Thread(Server::startServer);
        serverThread.start();
        TimeUnit.MILLISECONDS.sleep(500); 
    }

    @AfterEach
    void stopServerThread() {
        Server.stopServer();
        try {
            serverThread.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testClientConnectionAndGameStart() {
        try (Socket client = new Socket("localhost", PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            assertTrue(client.isConnected(), "Client should be connected");

            assertResponseContains(in, ">");

            send(out, "1");
            assertResponseContains(in, ">");

            send(out, "TestPlayer");
            assertResponseContains(in, ">");
            
            send(out, "1");

            assertHasOutput(in);

            send(out, "exit");
            send(out, "quit");
            
        } catch (IOException e) {
            fail("IOException during client-server interaction: " + e.getMessage());
        }
    }

    private void send(PrintWriter out, String message) {
        out.println(message);
    }

    private void assertResponseContains(BufferedReader in, String expectedFragment) throws IOException {
        StringBuilder buffer = new StringBuilder();
        long start = System.currentTimeMillis();
        boolean found = false;
        
        while (System.currentTimeMillis() - start < 3000 && !found) {
            if (in.ready()) {
                int c = in.read();
                if (c != -1) {
                    buffer.append((char) c);
                    if (buffer.toString().contains(expectedFragment)) {
                        found = true;
                    }
                }
            }
        }
        assertTrue(found, "Response timed out waiting for: " + expectedFragment);
    }

    private void assertHasOutput(BufferedReader in) throws IOException {
        long start = System.currentTimeMillis();
        boolean found = false;
        while (System.currentTimeMillis() - start < 2000) {
             if (in.ready()) {
                int c = in.read();
                if (c != -1) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue(found, "Should receive game output after class selection");
    }
}
