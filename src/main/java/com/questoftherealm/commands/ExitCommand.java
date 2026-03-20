package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.SaveGame;
import com.questoftherealm.server.ServerLogger;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit");
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("exit.description");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }

        try {
            if (state.isPrivate()) {
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.prompt.save"));
                String response = state.getGameServices().getInput().nextLine().trim().toUpperCase();
                if (response.startsWith("Y")) {
                    state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.prompt.saveName"));
                    String fileName = state.getGameServices().getInput().nextLine().trim();
                    state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.info.saving"));
                    SaveGame saveGame = new SaveGame();
                    saveGame.createSave(fileName, player, state);
                    state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.success.saved"));
                } else {
                    state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.info.notSaved"));
                }
                state.setGameOver(true);
            }

            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.info.farewell"));

            if (state.getActivePlayers().isEmpty()) {
                state.setGameOver(true);
            }

            if (player.isActive()) {
                player.setActive(false);
            }

        } catch (SaveError e) {
            ServerLogger.get().error("ExitCommand: Failed to save game for player " + player.getName() + ": " + e.getMessage(), e);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.error.saving", e.getMessage()));
        }
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player, state);
    }



}