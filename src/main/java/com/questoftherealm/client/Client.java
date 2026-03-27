package com.questoftherealm.client;

import com.questoftherealm.localization.MessageBundle;
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
                    String firstLine = in.readLine();
                    if (firstLine != null && firstLine.equals("REJECTED")) {
                        consoleOut.println(bundle.get("server.maxPlayers"));
                        return;
                    }

                    if (firstLine != null) {
                        consoleOut.println(firstLine);
                    }

                    int serverCharacter;
                    while ((serverCharacter = in.read()) != -1) {
                        consoleOut.print((char) serverCharacter);
                        consoleOut.flush();
                    }
                } catch (IOException e) {
                    consoleOut.println(bundle.get("client.disconnected"));
                }
            });
            listener.setDaemon(true);
            listener.start();

            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                out.println(input);
                if (input.equalsIgnoreCase("quit")) {
                    consoleOut.println(bundle.get("client.disconnected"));
                    break;
                }
            }

        } catch (IOException e) {
            consoleOut.println(bundle.get("client.error.network"));
        }
        catch (Exception e) {
            consoleOut.println(bundle.get("client.error.unexpected"));
        }
    }

    static void main() {
        bundle = new MessageBundle();
        try (Socket socket = new Socket("localhost", SERVER_PORT)) {
            new Client().handleConnection(socket, System.in, System.out);
        } catch (IOException e) {
            String msg = bundle.get("client.error.connection", String.valueOf(SERVER_PORT));
            System.out.println(msg);
        } catch (Exception e) {
            System.out.println(bundle.get("client.error.unexpected"));
        }
    }
}
