package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameLoop;
import com.questoftherealm.game.SaveGame;

import java.util.Scanner;

public class ExitCommand extends Command {
    private Scanner scanner = new Scanner(System.in);

    public ExitCommand() {
        super("exit");
    }

    @Override
    public String getDescription() {
        return "exit — saves and closes the game (prompts before quitting)";
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args, player)){
            return;
        }
        System.out.println("Would you like to save your progress? Y/N");
        String response = scanner.nextLine().trim().toUpperCase();

        try {
            if (response.startsWith("Y")) {
                System.out.println("Save name: ");
                String fileName = scanner.nextLine().trim();
                System.out.println("Saving game...");
                SaveGame saveGame = new SaveGame();
                saveGame.createSave(fileName);
                System.out.println("✅ Game saved successfully.");
            } else {
                System.out.println("Progress not saved.");
            }

            System.out.println("👋 Exiting game. See you next time, adventurer!");
            player.trackPlayTime();
            Thread.sleep(800);
            System.exit(0);

        } catch (Exception e) {
            System.out.println("⚠️ Error while saving the game: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 1) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

}
