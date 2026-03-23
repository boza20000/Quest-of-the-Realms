package com.questoftherealm.client;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void givenRunningServer_whenClientConnects_thenCommunicationIsSuccessful() throws IOException, InterruptedException {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            Thread serverThread = getThread(serverSocket);
            serverThread.start();
            
            String simulatedUserInput = "Hello\nquit\n";
            ByteArrayInputStream consoleIn = new ByteArrayInputStream(simulatedUserInput.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream consoleOut = new ByteArrayOutputStream();
            PrintStream validationOut = new PrintStream(consoleOut, true, StandardCharsets.UTF_8);

            try (Socket socket = new Socket("localhost", port)) {
                Client client = new Client();
                client.handleConnection(socket, consoleIn, validationOut);
            }
            
            serverThread.join(2000);
            
            String output = consoleOut.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains("Welcome"), "Client output should contain server message. Actual: " + output);
        }
    }

    private static Thread getThread(ServerSocket serverSocket) {
        return new Thread(() -> {
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                out.println("Welcome");

                String line = in.readLine(); 
                assertTrue(line.equals("Hello") || line.equals("quit"), "Server should receive 'Hello' or 'quit' from client. Actual: " + line);
                
            } catch (IOException e) {
            }
        });
    }
}

