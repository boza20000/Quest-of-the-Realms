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
import java.io.PrintWriter;
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
        Thread.currentThread().setName("Server Thread");
        running = true;

        ExecutorService executor;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

            logServerStarted();
            acceptClientsLoop(executor);
            
            shutdownExecutor(executor);
            
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            System.out.println(bundle.get("server.error.socket"));
        } finally {
            cleanupServerSocket();
        }
    }

    private static void logServerStarted() throws IOException {
        InetAddress serverAddress = InetAddress.getLocalHost();
        GameState tempState = new GameState(null, new GameServices(new ConsoleOutput(), new ConsoleInput()));
        String startedMsg = tempState.getMessages().getBundle().get("server.started",
                serverAddress.getHostAddress(), String.valueOf(SERVER_PORT));
        System.out.println(startedMsg);
        log.info(startedMsg);
    }

    private static void acceptClientsLoop(ExecutorService executor) {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                
                if (isMaxPlayersReached()) {
                    rejectClient(clientSocket);
                    continue;
                }
                
                handleClientConnection(executor, clientSocket);
                
            } catch (IOException e) {
                if (!running) break;
                log.error("Error accepting client connection", e);
            }
        }
    }

    private static boolean isMaxPlayersReached() {
        return counter.get() >= MAX_EXECUTOR_THREADS;
    }

    private static void rejectClient(Socket clientSocket) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("REJECTED");
            clientSocket.close();
        } catch (IOException e) {
            log.warn("Error rejecting client: " + clientSocket.getInetAddress(), e);
        }
        
        String rejectedMsg = "Client rejected - max players reached: " + clientSocket.getInetAddress();
        System.out.println(rejectedMsg);
        log.warn(rejectedMsg);
    }

    private static void handleClientConnection(ExecutorService executor, Socket clientSocket) throws IOException {
        counter.incrementAndGet();

        masterState = new GameState(null,
                new GameServices(new ClientSocketOutput(clientSocket), new ClientSocketInput(clientSocket)));
        
        logClientAccepted(clientSocket);

        ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket,
                counter, masterState, activeGames, activePlayers);
        executor.execute(clientHandler);

        logActivePlayerCount();
    }

    private static void logClientAccepted(Socket clientSocket) {
        String acceptedMsg = masterState.getMessages().getBundle().get("server.accepted",
                clientSocket.getInetAddress(), clientSocket.getPort());
        System.out.println(acceptedMsg);
        log.info(acceptedMsg);
    }

    private static void logActivePlayerCount() {
        String activeMsg = masterState.getMessages().getBundle().get("server.playersActive",
                counter.get(), MAX_EXECUTOR_THREADS);
        System.out.println(activeMsg);
        log.info(activeMsg);
    }

    private static void shutdownExecutor(ExecutorService executor) {
        if (executor == null) return;
        
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate within timeout, forcing shutdown");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Server interrupted during shutdown", e);
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }

    private static void cleanupServerSocket() {
        if (!running || serverSocket == null || serverSocket.isClosed()) return;
        
        try {
            serverSocket.close();
        } catch (IOException e) {
            log.error("Error closing server socket", e);
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