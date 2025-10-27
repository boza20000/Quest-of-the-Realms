package com.questoftherealm.game;

import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.interaction.Console;
import com.questoftherealm.interaction.GameUI;
import com.questoftherealm.interaction.Interactions;
import com.questoftherealm.map.Map;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;

import java.io.IOException;
import java.util.Queue;

public class Game {
    private static Player player;
    private static Map gameMap;
    private static Queue<Quest> gameQuests;

    private final GameUI gameUI = new GameUI();
    private final Console console = new Console();

    public static boolean gameOver = false;


    public static Player getPlayer() {
        return player;
    }

    public static void setGameQuests(Queue<Quest> gameQuests) {
        Game.gameQuests = gameQuests;
    }

    public static void setPlayer(Player player) {
        Game.player = player;
    }

    public static Map getGameMap() {
        return gameMap;
    }

    public static Queue<Quest> getQuests() {
        return gameQuests;
    }

    public void newGame() {
        try {
            buildPlayerCharacter();
            gameUI.showIntro();
            gameUI.getConsole().clear();
            console.worldIntro();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGame() {
        LoadGame loadGame = new LoadGame();
        System.out.println("Which save you want to load?");
        loadGame.printSaves();
        String save = gameUI.getScanner().nextLine();
        System.out.println("Loading...");
        try {
            loadGame.loadGameSave(save);
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
        //map instance
        try {
            gameMap = Map.getInstance();
        } catch (Exception e) {
            System.out.println("Map unavailable");
            System.out.println("restart game");
            System.exit(0);
        }
        //quests instance
        try {
            new QuestFactory();
            gameQuests = QuestFactory.getQuests();
        } catch (Exception e) {
            System.out.println("Story Quests unavailable");
        }
        //load game type
        switch (gameType) {
            case 1 -> newGame();
            case 2 -> loadGame();
        }
        //game loop
        GameLoop loop = new GameLoop();
        loop.startLoop();
    }

}
