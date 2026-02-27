package com.questoftherealm.game;

import com.questoftherealm.exceptions.FileNotLoaded;
import com.questoftherealm.exceptions.IntroException;
import com.questoftherealm.exceptions.NpcInitializationFailed;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.ConsoleController;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;

import java.io.IOException;
import java.net.Socket;

public class Game {
    private final GameState gameState;
    private ConsoleController console;
    private final Output output;
    private final Socket socket;
    private final int gameRules;
    private final PlayerTypes characterType;
    private final String characterName;
    private final String serverRoom;

    public Game(Socket socket, GameState state, int rules, PlayerTypes characterType, String characterName,String serverRoom) {
        this.socket = socket;
        this.gameState = state;
        this.gameRules = rules;
        this.characterType = characterType;
        this.characterName = characterName;
        this.output = state.getGameServices().getOutput();
        this.serverRoom = serverRoom;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void start() {
        Player curPlayer = new Player(characterName, characterType, gameState);
        displayChoice(curPlayer);
        gameState.addPlayer(curPlayer);
        console = new ConsoleController(gameState);
        if (socket != null && socket.isConnected() && gameRules == 2) {
            output.println("Connected to server. Starting multiplayer game...");
            output.println("Player joined" + serverRoom + ": " + curPlayer.getName() + " the " + curPlayer.getPlayerType());

        } else {
            try {
                initializeSinglePlayerGame(curPlayer);
            } catch (FileNotLoaded | NpcInitializationFailed | IntroException e) {
                output.println(e.getMessage());
                return;
            }
        }

        console.displayTitle();

        GameLoop loop = new GameLoop();
        loop.startLoop(this, characterName);
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
        gameState.addPlayer(player);

        try {
            console.showIntro();
        } catch (IOException e) {
            throw new IntroException(gameState.getMessages().getBundle().get("game.new.error.introCorrupted"));
        }
        console.worldIntro();

    }

    public void loadGame(Player player) {
        LoadGame loadGame = new LoadGame();
        output.println(gameState.getMessages().getBundle().get("game.load.ask"));
        loadGame.printSaves(gameState);
        String save = gameState.getGameServices().getInput().nextLine();
        output.println(gameState.getMessages().getBundle().get("game.load.loading"));
        try {
            loadGame.loadGameSave(save, gameState);
        } catch (Exception e) {
            throw new FileNotLoaded(gameState.getMessages().getBundle().get("game.load.error"));
        }
    }

    public ConsoleController getConsole() {
        return console;
    }
}