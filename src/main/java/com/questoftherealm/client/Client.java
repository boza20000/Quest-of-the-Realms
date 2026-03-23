package com.questoftherealm.client;

import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.server.ServerLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private static final int SERVER_PORT = 2020;
    private static MessageBundle bundle;

    public void handleConnection(Socket socket, InputStream consoleIn, PrintStream consoleOut) {
        bundle = new MessageBundle();

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(consoleIn)) {

            Thread listener = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    int serverCharacter;
                    while ((serverCharacter = in.read()) != -1) {
                        consoleOut.print((char) serverCharacter);
                        consoleOut.flush();
                    }
                } catch (IOException e) {
                    ServerLogger.get().warn("Client: IOException in listener thread, connection closed", e);
                    consoleOut.println(bundle.get("client.disconnected"));
                }
            });
            listener.start();

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                out.println(input);
                if (input.equalsIgnoreCase("quit")) break;
            }

        } catch (IOException e) {
            ServerLogger.get().error("Client: IOException in handleConnection", e);
            throw new RuntimeException(bundle.get("client.error.network"), e);
        }
    }

    static void main() {
        try (Socket socket = new Socket("localhost", SERVER_PORT)) {
            new Client().handleConnection(socket, System.in, System.out);
        } catch (IOException e) {
            bundle = new MessageBundle();
            ServerLogger.get().error("Client: Failed to connect to server at localhost:" + 2020, e);
            throw new RuntimeException(bundle.get("client.error.network"), e);
        }
    }
}
