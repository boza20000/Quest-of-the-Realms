package com.questoftherealm.game;

import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.maps.Map;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private static Player player;
    private static Map gameMap;
    private final GameUI gameUI = new GameUI();
    public static boolean gameOver = false;
    public static Queue<Quest> gameQuests;

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        Game.player = player;
    }

    public static Map getGameMap() {return gameMap;}

    public static Queue<Quest> getQuests() {return gameQuests;}

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void buildPlayerCharacter() {
        String name = gameUI.characterCreationScreen();
        int count = 0;
        int typeChoice;
        while (true) {
            try {
                System.out.print(">");
                typeChoice = Integer.parseInt(gameUI.getScanner().nextLine());
                if (typeChoice == 1 || typeChoice == 2 || typeChoice == 3 || typeChoice == 4) break;
                else {
                    count++;
                    if (count <= 1) System.out.println("Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                count++;
                if (count <= 1) System.out.println("Invalid input. Please enter a number between 1 and 4.");
            }
        }
        PlayerTypes type = PlayerTypes.fromInt(typeChoice);
        player = new Player(name, type);
        System.out.println("You have chosen " + type);
        System.out.println(player.getPlayerCharacter());
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        gameUI.getConsole().displayTitle();
        int gameType;
        int count = 0;
        while (true) {
            try {
                count++;
                gameType = gameUI.showMainMenu(count);
                if (gameType == 1 || gameType == 2) break;
            } catch (NumberFormatException e) {
                if (count <= 1) System.out.println("Invalid input. Please enter a number.");
                count++;
            }
        }
        switch (gameType) {
            case 1 -> newGame();
            case 2 -> loadGame();
        }
        try {
            gameMap = Map.getInstance();
        } catch (Exception e) {
            System.out.println("Map unavailable");
            System.out.println("restart game");
            System.exit(0);
        }
        try {
            new QuestFactory();
            gameQuests = QuestFactory.getQuests();
        } catch (Exception e) {
            System.out.println("Story Quests unavailable");
        }

        GameLoop loop = new GameLoop();
        loop.startLoop();
    }
}
