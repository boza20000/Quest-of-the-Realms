package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.LoadGame;
import com.questoftherealm.interaction.MissionInteractions;

public class LoadCommand extends Command {

    public LoadCommand() {
        super("load");
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("load.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (!state.isPrivate()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("load.error.notPrivate"));
            return false;
        }
        if (args.length != 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("load.usage", getDescription(state)));
            return false;
        }
        return true;
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }

        LoadGame loadGame = createLoadGame();
        loadGame.loadGameSave(args[1], state);

        state.removePlayer(player.getName());

        Player loaded = state.getActivePlayers().isEmpty() ? player : state.getActivePlayers().getFirst();
        MissionInteractions missionInteractions = createMissionInteractions(state);
        missionInteractions.worldStart(loaded);
    }

    protected LoadGame createLoadGame() {
        return new LoadGame();
    }

    protected MissionInteractions createMissionInteractions(GameState state) {
        return new MissionInteractions(state);
    }
}