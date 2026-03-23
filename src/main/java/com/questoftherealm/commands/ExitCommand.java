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
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("exit.description");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.prompt.save"));
        String response = state.getGameServices().getInput().nextLine().trim().toUpperCase();

        try {
            if (response.startsWith("Y")) {
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.prompt.saveName"));
                String fileName = state.getGameServices().getInput().nextLine().trim();
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.info.saving"));
                SaveGame saveGame = new SaveGame();
                saveGame.createSave(fileName, player,state);
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.success.saved"));
            } else {
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.info.notSaved"));
            }

            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.info.farewell"));
            player.trackPlayTime(state);
            Thread.sleep(800);
            //to remove replace with server stop
            //state.requestCloseGame();
            System.exit(0);

        } catch (Exception e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.error.saving", e.getMessage()));
            //to remove replace with server stop
            //state.requestCloseGame();
            System.exit(1);
        }
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("exit.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }

}