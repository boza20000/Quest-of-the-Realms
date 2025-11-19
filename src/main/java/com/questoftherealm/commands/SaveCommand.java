package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.SaveGame;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save");
    }

    @Override
    public String getDescription() {
        return "save [name] — saves your current game progress under the specified name";
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        if (args[1].isEmpty()) {
            state.getGameServices().getOutput().println("Name can't be empty");
            throw new SaveError("Save name is empty");
        }
        String fileName = args[1];

        try {
            SaveGame saveGame = new SaveGame();
            saveGame.createSave(fileName, player,state);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
