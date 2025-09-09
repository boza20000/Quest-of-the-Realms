package com.questoftherealm.game;

import com.questoftherealm.maps.Map;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;

import java.io.IOException;

public class Game {
    private static Player player;
    private static Map gameMap;
    private final GameUI gameUI = new GameUI();
    public static boolean gameOver = false;


    public static Player getPlayer() {
        return player;
    }

    public static Map getGameMap() {
        return gameMap;
    }

    public void newGame() {
        try {
            gameUI.showIntro();
            gameUI.getConsole().clear();
            buildPlayerCharacter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGame() {
        System.out.println("Which save you want to load?");
        LoadGame.printSaves();
        String save = gameUI.getScanner().nextLine();
        System.out.println("Loading...");
        try {
            LoadGame.loadGameSave(save);
        } catch (Exception e) {//make exception for loading files not found
            throw new RuntimeException(e);
        }
    }

    public void buildPlayerCharacter() {
        String name = gameUI.characterCreationScreen();
        int typeChoice = Integer.parseInt(gameUI.getScanner().nextLine());
        PlayerTypes type = PlayerTypes.fromInt(typeChoice);
        player = new Player(name, type);
        System.out.println("You have chosen " + type);
        System.out.println(player.getPlayerCharacter());
    }

    public void start() {
        gameUI.getConsole().prepare();//to do
        gameUI.getConsole().displayTitle();

        int gameType = gameUI.showMainMenu();
        switch (gameType) {
            case 1 -> newGame();
            case 2 -> loadGame();
            default -> System.out.println("Invalid choice");
        }
        gameMap = Map.getInstance();
        System.out.println("Game starts...");
        GameLoop loop = new GameLoop();
        loop.startLoop();
    }
}
