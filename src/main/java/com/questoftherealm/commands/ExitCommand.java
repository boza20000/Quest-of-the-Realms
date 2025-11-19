package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameLoop;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.interaction.Console;

import java.util.Scanner;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit");
    }

    @Override
    public String getDescription() {
        return "exit — saves and closes the game (prompts before quitting)";
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        state.getGameServices().getOutput().println("Would you like to save your progress? Y/N");
        String response = state.getGameServices().getInput().nextLine().trim().toUpperCase();

        try {
            if (response.startsWith("Y")) {
                state.getGameServices().getOutput().println("Save name: ");
                String fileName = state.getGameServices().getInput().nextLine().trim();
                state.getGameServices().getOutput().println("Saving game...");
                SaveGame saveGame = new SaveGame();
                saveGame.createSave(fileName, player,state);
                state.getGameServices().getOutput().println("✅ Game saved successfully.");
            } else {
                state.getGameServices().getOutput().println("Progress not saved.");
            }

            state.getGameServices().getOutput().println("👋 Exiting game. See you next time, adventurer!");
            player.trackPlayTime();
            Thread.sleep(800);
            System.exit(0);

        } catch (Exception e) {
            state.getGameServices().getOutput().println("⚠️ Error while saving the game: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }

}
