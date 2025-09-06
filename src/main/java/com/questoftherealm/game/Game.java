package com.questoftherealm.game;

import com.questoftherealm.maps.Map;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;

import java.io.IOException;
import java.util.Scanner;

import static com.questoftherealm.game.GameLoop.startLoop;


public class Game {
    private static Player player;
    private Map gameMap;
    private final Scanner scanner = new Scanner(System.in);
    public static boolean gameOver = false;
    private final Console console = new Console();
    private final Story story = new Story();

    public static Player getPlayer() {
        return player;
    }


    private void showIntro() throws IOException {
        final int delay = 30; // smaller = faster typing
        boolean skip = false;

        for (char c : story.getStory().toCharArray()) {
            // Check if user pressed Enter to skip
            if (System.in.available() > 0) {
                skip = true;
                // Clear input buffer
                while (System.in.available() > 0) {
                    System.in.read();
                }
                break;
            }
            System.out.print(c);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        if (skip) {
            System.out.println(story.getStory());
        }
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    private void giveOptions() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Quest of the Realms!\n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "Choose an option: ");

        int gameType = Integer.parseInt(scanner.nextLine());

        switch (gameType) {
            case 1 -> {
                try {
                    showIntro();
                    console.clear();
                    buildPlayerCharacter();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case 2 -> {
                System.out.println("Loading...");
                LoadGame load = new LoadGame();
                try {
                    load.loadGameSave();
                } catch (Exception e) {//make exception for loading files not found
                    throw new RuntimeException(e);
                }
            }
        }
        sc.close();
    }

    public void buildPlayerCharacter() {
        System.out.println("Choose your name:");
        String name = scanner.nextLine();

        System.out.println("Choose your character:");
        System.out.println("1. Warrior — A strong fighter with high health and defense.");
        System.out.println("2. Mage — A master of spells, fragile but devastating.");
        System.out.println("3. Orc — Brutal and tough, with raw strength and resilience.");
        System.out.println("4. Rogue — Quick and cunning, excels at stealth and critical strikes.");

        int typeChoice = Integer.parseInt(scanner.nextLine());
        PlayerTypes type = PlayerTypes.fromInt(typeChoice);
        player = new Player(name, type);
        System.out.println("You have chosen " + type);
        System.out.println(player.getPlayerCharacter());
    }

    public void start() {
        console.prepare();//to do
        console.displayTitle();
        giveOptions();
        gameMap.getGameMap();
        System.out.println("Game starts...");
        startLoop();
    }
}
