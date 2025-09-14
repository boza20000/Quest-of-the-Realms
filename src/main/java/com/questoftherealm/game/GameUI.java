package com.questoftherealm.game;

import java.io.IOException;
import java.util.Scanner;

public class GameUI {
    private final Story story = new Story();
    private final Scanner scanner = new Scanner(System.in);
    private final Console console = new Console();

    public void showIntro() throws IOException {
        final int delay = 30; // smaller = faster typing
        boolean skip = false;
        System.out.println("Press Enter to skip the story");

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
            //System.out.println(story.getStory());
            System.out.print(GameConstants.RESET);
            return;
        }
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    public int showMainMenu(int count) {
        if (count <= 1) {
            System.out.println(
                    "1. New Game\n" +
                            "2. Load Game\n" +
                            "Type 1 for new game or 2 to load one: ");
            System.out.print(">");
        }
        else{
            System.out.print(">");
        }
        return Integer.parseInt(scanner.nextLine());
    }

    public String characterCreationScreen() {
        System.out.println("Choose your name:");
        String name = getScanner().nextLine();

        System.out.println("Choose your character:");
        System.out.println("1. Warrior — A strong fighter with high health and defense.");
        System.out.println("2. Mage — A master of spells, fragile but devastating.");
        System.out.println("3. Orc — Brutal and tough, with raw strength and resilience.");
        System.out.println("4. Rogue — Quick and cunning, excels at stealth and critical strikes.");
        return name;
    }

    public void handleLine() {
        System.out.print("\033[1A\033[2K\r" + " (No such command exists)");
    }


    public Scanner getScanner() {
        return scanner;
    }

    public Console getConsole() {
        return console;
    }
}
