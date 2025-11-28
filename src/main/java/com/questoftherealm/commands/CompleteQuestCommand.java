package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;

import java.util.List;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("progress");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        if (player.getCurQuest() == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("progress.error.noQuestLoaded"));
            return;
        }
        if (player.getCurMission() == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("progress.error.noMissionLoaded"));
            return;
        }
        List<Mission> missions = List.of();
        if (player.getCurQuest() != null) {
            try {
                if(player.getCurQuest() !=null) {
                    missions = player.getCurQuest().getMissions();
                }
                else{
                    state.getGameServices().getOutput().println(state.getMessages().getBundle().get("progress.info.allQuestsDone"));
                }
            } catch (NullPointerException e) {
                e.getSuppressed();
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("progress.error.missionsUnavailable"));
            }
        }
        if (!missions.isEmpty()) {
            String completedSymbol = state.getMessages().getBundle().get("progress.symbol.completed");
            String incompleteSymbol = state.getMessages().getBundle().get("progress.symbol.incomplete");
            for (Mission m : missions) {
                state.getGameServices().getOutput().println(m.getTask() + " " + ((m.isCompleted()) ? completedSymbol : incompleteSymbol));
            }
        } else {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("progress.info.noAvailableQuest"));
        }
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("progress.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("progress.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }

}