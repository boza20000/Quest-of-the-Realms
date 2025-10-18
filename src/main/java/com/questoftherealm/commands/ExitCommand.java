package com.questoftherealm.commands;

import com.questoftherealm.game.SaveGame;

import java.util.Scanner;

public class ExitCommand extends Command {
    private Scanner scanner = new Scanner(System.in);

    public ExitCommand() {
        super("exit");
    }

    @Override
    public String getDescription() {
        return "closes the game and saves it automatically";
    }

    @Override
    public void execute(String[] args) {

        System.out.println("Would you like to save your progress? Y/N");
        String response = scanner.nextLine().trim().toUpperCase();

        try {
            if (response.startsWith("Y")) {
                System.out.println("Save name: ");
                String fileName = scanner.nextLine().trim();
                System.out.println("Saving game...");
                SaveGame.saveGame(fileName);
                System.out.println("✅ Game saved successfully.");
            } else {
                System.out.println("Progress not saved.");
            }

            System.out.println("👋 Exiting game. See you next time, adventurer!");
            Thread.sleep(800);
            System.exit(0);

        } catch (Exception e) {
            System.err.println("⚠️ Error while saving the game: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
