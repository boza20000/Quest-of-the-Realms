package com.questoftherealm.server;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.client.ClientRequestHandler;
import com.questoftherealm.exceptions.ServerSocketException;
import com.questoftherealm.game.ClientSocketInput;
import com.questoftherealm.game.ClientSocketOutput;
import com.questoftherealm.game.ConsoleInput;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.localization.MessageBundle;

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
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final ConcurrentHashMap<String, GameState> activeGames = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Player> activePlayers = new ConcurrentHashMap<>();
    private static GameState masterState;
    private static final ServerLogger log = ServerLogger.get();
    private static MessageBundle bundle = new MessageBundle();
    private static volatile boolean running = true;
    private static ServerSocket serverSocket;

    public static void startServer() {
        Thread.currentThread().setName("Echo Server Thread");
        running = true;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

            InetAddress serverAddress = InetAddress.getLocalHost();
            GameState tempState = new GameState(null, new GameServices(new ConsoleOutput(), new ConsoleInput()));
            String startedMsg = tempState.getMessages().getBundle().get("server.started",
                    serverAddress.getHostAddress(), SERVER_PORT);
            System.out.println(startedMsg);
            log.info(startedMsg);

            Socket clientSocket;

            while (running) {
                if (counter.incrementAndGet() >= MAX_EXECUTOR_THREADS) {
                    String maxMsg = tempState.getMessages().getBundle().get("server.maxPlayers");
                    System.out.println(maxMsg);
                    log.warn(maxMsg);
                    break;
                }
                try {
                   clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    if (!running) {
                        break;
                    }
                    throw e;
                }

                masterState = new GameState(null,
                        new GameServices(new ClientSocketOutput(clientSocket), new ClientSocketInput(clientSocket)));
                String acceptedMsg = masterState.getMessages().getBundle().get("server.accepted",
                        clientSocket.getInetAddress(), clientSocket.getPort());
                System.out.println(acceptedMsg);
                log.info(acceptedMsg);

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket,
                        counter, masterState, activeGames, activePlayers);
                executor.execute(clientHandler);

                String activeMsg = masterState.getMessages().getBundle().get("server.playersActive",
                        counter.get(), MAX_EXECUTOR_THREADS);
                System.out.println(activeMsg);
                log.info(activeMsg);
            }
            executor.shutdown();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            System.out.println(bundle.get("server.error.socket"));
        }
    }

    public static void stopServer() {
        running = false;
        counter.set(0);
        activeGames.clear();
        activePlayers.clear();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error("Error closing server socket", e);
        }
    }

    static void main() {
        startServer();
    }
}