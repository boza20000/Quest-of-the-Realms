package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.server.ServerLogger;
import com.questoftherealm.server.ServerLogger;

public class SaveCommand extends Command {
    private SaveGame game;

    public SaveCommand() {
        super("save");
    }

    public SaveCommand(SaveGame game) {
        super("save");
        this.game = game;
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("save.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 2 && state.isPrivate()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("save.usage", getDescription(state)));
            return false;
        }
        if (args.length != 1 && !state.isPrivate()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("save.usage.multiplayer", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player, state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        if (game == null) {
            game = new SaveGame();
        }
        if (!state.isPrivate()) {
            makeMultiPlayerSave(player, state, game);
            return;
        }
        makeSinglePlayerSave(args, player, state, game);
    }

    private void makeMultiPlayerSave(Player player, GameState state, SaveGame saveGame) {
        try {
            saveGame.createSave(player, state);
        } catch (SaveError e) {
            ServerLogger.get().error("SaveCommand: Failed to create multiplayer save for player " + player.getName(), e);
            state.getGameServices().getOutput().println(e.getMessage());
        }
    }

    private void makeSinglePlayerSave(String[] args, Player player, GameState state, SaveGame saveGame) {
        if (args[1].isEmpty()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("save.error.emptyName"));
            throw new SaveError(state.getMessages().getBundle().get("save.error.saveNameEmpty"));
        }
        String fileName = args[1];

        try {
            saveGame.createSave(fileName, player, state);
        } catch (SaveError e) {
            ServerLogger.get().error("SaveCommand: Failed to create save file " + fileName + " for player " + player.getName(), e);
            state.getGameServices().getOutput().println(e.getMessage());
        }
    }
}