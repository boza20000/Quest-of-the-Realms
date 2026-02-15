package com.questoftherealm.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private static BufferedReader reader;
    private static PrintWriter writer;
    private static int SERVER_PORT = 2020;
    private static SocketChannel clientChannel;


    static void main() {
        startClient();
    }

    private static void startClient() {
        try(SocketChannel socketChannel = SocketChannel.open();
            Scanner scanner = new Scanner(System.in);) {
            initializeConnection(socketChannel);

            while(true){
                System.out.print("Enter message: ");
                String message = scanner.nextLine();


                System.out.println("Sending message <" + message + "> to the server...");
                writer.println(message);

                if (message.contains("disconnect")) {
                    handleDisconnect();
                }
                else{
                    if(processCommand()){
                        return;
                    }
                }

            }

        }catch (IOException e){
            //
        }


    }

    private static boolean processCommand() {
        try{
            String reply = reader.readLine();
            if (reply == null) {
                System.out.println("Connection closed by server.");
                return true;
            }

        }
        catch (IOException e){

        }
        return false;
    }

    private static void handleDisconnect() throws IOException {
        clientChannel.close();

    }


    private static void initializeConnection(SocketChannel socketChannel) throws IOException {
        socketChannel.connect(new InetSocketAddress("localhost", SERVER_PORT));
        System.out.println("Connected to the Game server.");
        refreshStreams(socketChannel);
    }

    private static void refreshStreams(SocketChannel socketChannel) {
        reader = new BufferedReader(Channels.newReader(socketChannel, "UTF-8"));
        writer = new PrintWriter(Channels.newWriter(socketChannel, "UTF-8"), true);
    }
}
