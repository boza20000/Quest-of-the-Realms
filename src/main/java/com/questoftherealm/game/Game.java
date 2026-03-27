package com.questoftherealm.game;

import com.questoftherealm.exceptions.CorruptedFileException;
import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.IntroException;
import com.questoftherealm.exceptions.NpcInitializationFailed;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.ConsoleController;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.server.ServerLogger;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class Game {
    private final GameState gameState;
    private ConsoleController console;
    private final Output output;
    private final Socket socket;
    private final int gameRules;
    private final PlayerTypes characterType;
    private final String characterName;
    private final GameServices playerServices;
    private volatile boolean isRunning = true;
    private boolean isFirst;
    private Map<String, Player> activePlayer;

    public Game(Socket socket, GameState state, int rules, PlayerTypes characterType, String characterName, GameServices playerServices, boolean isFirst, Map<String, Player> activePlayer) {
        this.socket = socket;
        this.gameState = state;
        this.gameRules = rules;
        this.characterType = characterType;
        this.characterName = characterName;
        this.output = playerServices.getOutput();
        this.playerServices = playerServices;
        isRunning = true;
        this.isFirst = isFirst;
        this.activePlayer = activePlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void start() {

        gameState.bindThreadServices(playerServices);
        Player curPlayer = new Player(characterName, characterType, gameState);
        prepareGame(curPlayer);

        if (socket != null && socket.isConnected() && gameRules == 2) {
            initializeMultiPlayerGame(curPlayer, isFirst);
        } else {
            try {
                initializeSinglePlayerGame(curPlayer);

            } catch (FileNotLoaded | NpcInitializationFailed | IntroException e) {
                output.println(e.getMessage());
                return;
            }
        }

        GameLoop loop = new GameLoop();
        loop.startLoop(this, characterName);
    }

    private void prepareGame(Player curPlayer) {
        activePlayer.put(characterName, curPlayer);
        displayChoice(curPlayer);
        gameState.addPlayer(curPlayer);
        console = new ConsoleController(gameState);
    }

    private void displayChoice(Player currentPlayer) {
        gameState.getGameServices().getOutput().println(gameState.getMessages().getBundle().get("game.player.choice.confirm", currentPlayer.getPlayerType()));
        gameState.getGameServices().getOutput().println(gameState.getMessages().getBundle().get("game.player.character.show", currentPlayer.getPlayerCharacter().stats(gameState)));
    }

    private void initializeSinglePlayerGame(Player player) {
        int gameMode = getGameMode();
        switch (gameMode) {
            case 1 -> newGame(player);
            case 2 -> loadGame(player);
        }
        NpcInitializer npcInitializer = new NpcInitializer();
        npcInitializer.registerAll(gameState);

        console.displayTitle();
    }

    private void initializeMultiPlayerGame(Player player, boolean isHost) {
        output.println(gameState.getMessages().getBundle().get("game.multiplayer.connected"));
        output.println(gameState.getMessages().getBundle().get("game.multiplayer.playerJoined", gameState.getName(), player.getName(), player.getPlayerType()));
        LoadGame loadGame = new LoadGame();
        try {
            loadGame.loadServerSave(gameState, player);
            console.displayTitle();
        } catch (FileNotLoaded e) {
            output.println(gameState.getMessages().getBundle().get("game.multiplayer.saveNotFound"));
            console.displayTitle();
            printGameStart();
        }  catch (CorruptedFileException e) {
            output.println(gameState.getMessages().getBundle().get("game.multiplayer.saveCorrupted"));
            console.displayTitle();
            printGameStart();
        }

        if (isHost) {
            NpcInitializer npcInitializer = new NpcInitializer();
            npcInitializer.registerAll(gameState);
        }
    }

    private void printGameStart() {
        try {
            console.showIntro();
        } catch (IOException e) {
            throw new IntroException(gameState.getMessages().getBundle().get("game.new.error.introCorrupted"));
        }
        console.worldIntro();
    }

    private int getGameMode() {
        int count = 0;
        int type;
        while (true) {
            try {
                count++;
                type = console.showMainMenu(count);
                if (type == 1 || type == 2) break;
            } catch (NumberFormatException e) {
                if (count <= 1) output.println(gameState.getMessages().getBundle().get("game.start.invalidInput"));
                count++;
            }
        }
        return type;
    }

    public void newGame(Player player) {
        printGameStart();
    }

    public void loadGame(Player player) {
        LoadGame loadGame = new LoadGame();

        if (!loadGame.hasSaves()) {
            output.println(gameState.getMessages().getBundle().get("game.load.noSaves"));
            newGame(player);
            return;
        }
        
        output.println(gameState.getMessages().getBundle().get("game.load.ask"));
        loadGame.printSaves(gameState);
        
        String save;
        int attempts = 0;
        while (true) {
            output.print(gameState.getMessages().getBundle().get("console.menu.prompt"));
            output.flush();
            save = gameState.getGameServices().getInput().nextLine().trim();

            if (save.isBlank()) {
                if (attempts < 1) {
                    output.println(gameState.getMessages().getBundle().get("game.load.emptyInput"));
                }
                attempts++;
                continue;
            }
            break;
        }

        if (save.equalsIgnoreCase("back")) {
            return;
        }
        
        output.println(gameState.getMessages().getBundle().get("game.load.loading"));
        try {
            loadGame.loadGameSave(save, gameState);
        } catch (FileNotLoaded | CorruptedFileException e) {
            ServerLogger.get().error("Game: Failed to load game save: " + save, e);
            throw new FileNotLoaded(gameState.getMessages().getBundle().get("game.load.error"));
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public ConsoleController getConsole() {
        return console;
    }
}