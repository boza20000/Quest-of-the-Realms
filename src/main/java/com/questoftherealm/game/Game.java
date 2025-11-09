package com.questoftherealm.game;

import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.interaction.Console;
import com.questoftherealm.interaction.GameUI;
import com.questoftherealm.map.Map;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.localization.MessageBundle;

import java.io.IOException;

public class Game {
    private static Player player;
    private static Map gameMap;
    private final GameUI gameUI = new GameUI();
    private final Console console = new Console();
    public static boolean gameOver = false;
    public static boolean isSimulation = false;

    public static boolean isSimulation() { return isSimulation; }
    public static void setSimulation(boolean simulation) { isSimulation = simulation; }
    public static Player getPlayer() { return player; }
    public static void setPlayer(Player player) { Game.player = player; }
    public static Map getGameMap() { return gameMap; }

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
        System.out.println(MessageBundle.get("game.load.ask"));
        loadGame.printSaves();
        String save = gameUI.getScanner().nextLine();
        System.out.println(MessageBundle.get("game.load.loading"));
        try {
            loadGame.loadGameSave(save);
        } catch (Exception e) {
            System.out.println(MessageBundle.get("game.load.error"));
            throw new RuntimeException(e);
        }
    }

    public void buildPlayerCharacter() {
        String name = gameUI.characterCreationScreen();
        int count = 0;
        int typeChoice;

        while (true) {
            try {
                System.out.print(MessageBundle.get("game.choice.prompt"));
                typeChoice = Integer.parseInt(gameUI.getScanner().nextLine());
                if (typeChoice >= 1 && typeChoice <= 4) break;
                else {
                    count++;
                    if (count <= 1) System.out.println(MessageBundle.get("game.choice.invalidRange"));
                }
            } catch (NumberFormatException e) {
                count++;
                if (count <= 1) System.out.println(MessageBundle.get("game.choice.invalidInput"));
            }
        }

        PlayerTypes type = PlayerTypes.fromInt(typeChoice);
        player = new Player(name, type);

        System.out.println(MessageBundle.get("game.player.choice.confirm", type));
        System.out.println(MessageBundle.get("game.player.character.show", player.getPlayerCharacter()));

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            System.out.println(MessageBundle.get("game.sleep.error"));
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
                if (count <= 1) System.out.println(MessageBundle.get("game.start.invalidInput"));
                count++;
            }
        }

        try {
            gameMap = Map.getInstance();
        } catch (Exception e) {
            System.out.println(MessageBundle.get("game.map.unavailable"));
            System.out.println(MessageBundle.get("game.map.restart"));
            System.exit(0);
        }

        switch (gameType) {
            case 1 -> newGame();
            case 2 -> loadGame();
        }

        try {
            NpcInitializer npcInitializer = new NpcInitializer();
            npcInitializer.registerAll(gameMap);
        } catch (Exception e) {
            System.out.println(MessageBundle.get("game.npc.init.error"));
            System.exit(0);
        }

        GameLoop loop = new GameLoop();
        loop.startLoop();
    }
}
