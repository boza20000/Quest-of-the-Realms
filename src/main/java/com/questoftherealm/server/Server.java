package com.questoftherealm.server;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.client.ClientRequestHandler;
import com.questoftherealm.game.ClientSocketInput;
import com.questoftherealm.game.ClientSocketOutput;
import com.questoftherealm.game.ConsoleInput;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    private static final int SERVER_PORT = 2020;
    private static final int MAX_EXECUTOR_THREADS = 15;
    private static AtomicInteger counter = new AtomicInteger(0);
    private static ConcurrentHashMap<String, GameState> activeGames = new ConcurrentHashMap<>();
    private static GameState masterState;


    static void main() {
        Thread.currentThread().setName("Echo Server Thread");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
             ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS)) {

            InetAddress serverAddress = InetAddress.getLocalHost();
            System.out.println(" Game server started on " + serverAddress.getHostAddress()
                    + " and listening on port " + SERVER_PORT);

            Socket clientSocket;

            while (true) {
                if (counter.incrementAndGet() >= MAX_EXECUTOR_THREADS) {
                    System.out.println("Game server max player reached");
                    break;
                }
                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                masterState = new GameState(null, new GameServices(new ClientSocketOutput(clientSocket), new ClientSocketInput(clientSocket)));
                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, counter, masterState,activeGames);
                executor.execute(clientHandler);
                System.out.println("Players active: (" + counter.get() + "/" + MAX_EXECUTOR_THREADS + ")");
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }

}