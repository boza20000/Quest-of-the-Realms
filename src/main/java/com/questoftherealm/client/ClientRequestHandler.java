package com.questoftherealm.client;

import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.ConsoleController;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private AtomicInteger counter;
    private GameState masterState;
    private int choice;
    private String username;
    private PlayerTypes type;
    private String serverRoom;
    private Map<String, GameState> activeGames;

    public ClientRequestHandler(Socket socket, AtomicInteger counter, GameState masterState, Map<String, GameState> activeServers) {
        this.socket = socket;
        this.counter = counter;
        this.masterState = masterState;
        this.activeGames = activeServers;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());
        try {
            setRules();
            masterState = serverRoom != null ? activeGames.get(serverRoom) : masterState;
            Game game = new Game(socket, masterState, choice, type, username, serverRoom);
            game.start();

        } catch (Exception e) {
            System.out.println("Game error.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //throw new RuntimeException(e);
            }
            System.out.println("Player left. " + "(" + counter.decrementAndGet() + ")");
        }

    }

    private void setRules() {
        ConsoleController console = new ConsoleController(masterState);
        Output out = masterState.getGameServices().getOutput();
        out.println("Connected to the Quest of the Realm Server!");
        out.println("Select Game Rules:\n1. Single world\n2. Connect to server");
        try {
            out.print(masterState.getMessages().getBundle().get("console.menu.prompt"));
            out.flush();
            this.choice = Integer.parseInt(masterState.getGameServices().getInput().nextLine());
        } catch (NumberFormatException e) {
            this.choice = 1;
        }
        if (choice != 2) {
            out.println("You have chosen singleplayer");
            masterState.setPrivate(true);
        } else {
            //check for joining singleplayer rooms
            out.println("You have chosen multiplayer");
            out.println("Do you want to create a new room or join an existing one? (Type the name of the room you want to join or create.If you want to join singleplayer(type it))");
            String gameRoom = roomChoice(out);
            if (gameRoom == null) {
                out.println("You have chosen singleplayer");
                this.choice = 1;
                masterState.setPrivate(true);
            }
        }

        this.username = console.characterCreationScreen();
        this.type = buildPlayerCharacter(out);

    }

    private String roomChoice(Output out) {
        printRoomsOptions(out);
        String room = masterState.getGameServices().getInput().nextLine();

        if (activeGames.containsKey(room)) {
            out.println("Joining room: " + room);
            serverRoom = room;
            return room;
        } else if (room.equalsIgnoreCase("singleplayer")) {
            out.println("Joining singleplayer instead.");
            masterState.setPrivate(true);
            return null;
        } else {
            out.println("Creating new room." + room);
            return room;
        }
    }

    private void printRoomsOptions(Output out) {
        for (String roomName : activeGames.keySet().stream().toList()) {
            out.println("- " + roomName);
        }
        out.println("Type the name of the room you want to join or create.If you want to join singleplayer(type it)");
        out.flush();
    }

    private PlayerTypes buildPlayerCharacter(Output output) {
        int count = 0;
        int typeChoice;

        while (true) {
            try {
                output.print(masterState.getMessages().getBundle().get("console.menu.prompt"));
                output.flush();
                typeChoice = Integer.parseInt(masterState.getGameServices().getInput().nextLine());
                if (typeChoice >= 1 && typeChoice <= 4) break;
                else {
                    count++;
                    if (count <= 1)
                        output.println(masterState.getMessages().getBundle().get("game.choice.invalidRange"));
                }
            } catch (NumberFormatException e) {
                count++;
                if (count <= 1) output.println(masterState.getMessages().getBundle().get("game.choice.invalidInput"));
            }
        }

        return PlayerTypes.fromInt(typeChoice, masterState);
    }
}