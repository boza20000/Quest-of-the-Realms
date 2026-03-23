package com.questoftherealm.client;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.NpcInitializationFailed;
import com.questoftherealm.exceptions.IntroException;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.ConsoleController;
import com.questoftherealm.server.ServerLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//take client to the room and make character choices
public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private AtomicInteger counter;
    private GameState masterState;
    private int gameChoice;
    private String username;
    private PlayerTypes type;
    private String serverRoom;
    private Map<String, GameState> activeGames;
    private boolean creatingNewRoom = false;
    private Map<String, Player> activePlayer;
    private static final ServerLogger log = ServerLogger.get();

    public ClientRequestHandler(Socket socket, AtomicInteger counter, GameState masterState, Map<String, GameState> activeServers, Map<String, Player> activePlayer) {
        this.socket = socket;
        this.counter = counter;
        this.masterState = masterState;
        this.activeGames = activeServers;
        this.activePlayer = activePlayer;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());
        try {
            GameServices playerServices = masterState.getGameServices();
            setRules();
            masterState = serverRoom != null ? activeGames.get(serverRoom) : masterState;
            Game game = new Game(socket, masterState, gameChoice, type, username, playerServices, creatingNewRoom, activePlayer);
            game.start();

        } catch (FileNotLoaded | NpcInitializationFailed | IntroException e) {
            String msg = masterState.getMessages().getBundle().get("client.handler.gameError");
            System.out.println(msg);
            log.error(msg + " | Player: " + username + " | Room: " + serverRoom, e);
        } finally {
            try {
                handleLeavingPlayer();
            } catch (IOException e) {
                log.error("Error while handling leaving player: " + username, e);
            }
            String leftMsg = masterState.getMessages().getBundle().get("client.handler.playerLeft", counter.decrementAndGet());
            System.out.println(leftMsg);
            log.info(leftMsg + " | Player: " + username);
        }

    }

    private void handleLeavingPlayer() throws IOException {
        saveGame();
        removeLeaver();
        socket.close();
    }

    private void removeLeaver() {
        if (username != null) {
            masterState.removePlayer(username);
            activePlayer.remove(username);
        }

        if (masterState.getActivePlayers().isEmpty()) {
            if (serverRoom != null && activeGames.containsKey(serverRoom)) {
                activeGames.remove(serverRoom);
                System.out.println(masterState.getMessages().getBundle().get("client.handler.roomRemoved", serverRoom));
            }
            if (masterState.isPrivate()) {
                masterState.setGameOver(true);
            }
        }
    }

    private void saveGame() {
        if (username == null || username.isEmpty() || type == null) {
            String msg = masterState.getMessages().getBundle().get("client.handler.noSave");
            System.out.println(msg);
            log.warn(msg);
            return;
        }
        SaveGame saveGame = new SaveGame();
        if (masterState.isPrivate()) {
            String saveName = username + "_save_" + getReadableTimestamp();
            saveGame.createSave(saveName, activePlayer.get(username), masterState);
            return;
        }
        try {
            saveGame.createSave(activePlayer.get(username), masterState);
        } catch (SaveError e) {
            String msg = masterState.getMessages().getBundle().get("client.handler.autoSaveFail", username);
            System.out.println(msg);
            log.error(msg, e);
        }
    }

    private void setRules() {
        ConsoleController console = new ConsoleController(masterState);
        Output out = masterState.getGameServices().getOutput();
        out.println(masterState.getMessages().getBundle().get("client.handler.welcome"));
        out.println(masterState.getMessages().getBundle().get("client.handler.selectRules"));
        out.println(masterState.getMessages().getBundle().get("client.handler.rules.options"));
        try {
            out.print(masterState.getMessages().getBundle().get("console.menu.prompt"));
            out.flush();
            this.gameChoice = Integer.parseInt(masterState.getGameServices().getInput().nextLine());
        } catch (NumberFormatException e) {
            this.gameChoice = 1;
        }
        if (gameChoice != 2) {
            out.println(masterState.getMessages().getBundle().get("client.handler.chose.singleplayer"));
            masterState.setPrivate(true);
        } else {
            out.println(masterState.getMessages().getBundle().get("client.handler.chose.multiplayer"));
            out.println(masterState.getMessages().getBundle().get("client.handler.room.joinOrCreate"));
            String gameRoom = roomChoice(out, console);
            if (gameRoom == null) {
                out.println(masterState.getMessages().getBundle().get("client.handler.chose.singleplayer"));
                this.gameChoice = 1;
                masterState.setPrivate(true);
            }
        }
        try {
            this.username = console.usernameCreationScreen(activePlayer);
            this.type = console.characterCreationScreen(out, console, masterState);
            if (creatingNewRoom) {
                activeGames.put(serverRoom, masterState);
            }
        }
        catch (RuntimeException e) {
            String msg = masterState.getMessages().getBundle().get("client.handler.creation.screen.error");
            System.out.println(msg);
            log.error(msg, e);
        }
    }

    private String roomChoice(Output out, ConsoleController console) {
        ScheduledExecutorService refresh = Executors.newSingleThreadScheduledExecutor();

        refresh.scheduleAtFixedRate(() -> {
            refreshProcess(out, console);
        }, 15, 15, TimeUnit.SECONDS);

        try {
            console.printRoomsOptions(out, activeGames);
            while (true) {
                String room = masterState.getGameServices().getInput().nextLine().trim();

                if (activeGames.containsKey(room)) {
                    if (activeGames.get(room).isPrivate()) {
                        out.println(masterState.getMessages().getBundle().get("client.handler.room.private"));
                        continue;
                    }
                    return joinRoom(out, room);

                } else if (room.equalsIgnoreCase("singleplayer")) {
                    return joinSinglePlayer(out);

                } else {
                    return createNewRoom(out, room);

                }
            }
        } finally {
            refresh.shutdownNow();
        }
    }

    private void refreshProcess(Output out, ConsoleController console) {
        out.println(masterState.getMessages().getBundle().get("client.handler.room.liveUpdate"));
        console.printRoomsOptions(out, activeGames);
        try {
            cleanBuffer();
        } catch (IOException e) {
            log.error("Error cleaning socket buffer for player: " + username, e);
        }
    }

    private String joinSinglePlayer(Output out) {
        out.println(masterState.getMessages().getBundle().get("client.handler.room.joinSingleplayer"));
        masterState.setPrivate(true);
        return null;
    }

    private String joinRoom(Output out, String room) {
        out.println(masterState.getMessages().getBundle().get("client.handler.room.joining", room));
        serverRoom = room;
        return room;
    }

    private String createNewRoom(Output out, String room) {
        creatingNewRoom = true;
        out.println(masterState.getMessages().getBundle().get("client.handler.room.creating", room));
        masterState = new GameState(room, masterState.getGameServices());
        serverRoom = room;
        return room;
    }

    private void cleanBuffer() throws IOException {
        InputStream in = socket.getInputStream();
        while (in.available() > 0) {
            in.skip(in.available());
        }
    }

    public String getReadableTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return now.format(formatter);
    }
}