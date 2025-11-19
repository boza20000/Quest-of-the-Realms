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
            state.getGameServices().getOutput().println("Error: No quest loaded.");
            return;
        }
        if (player.getCurMission() == null) {
            state.getGameServices().getOutput().println("Error: No mission loaded.");
            return;
        }
        List<Mission> missions = List.of();
        if (player.getCurQuest() != null) {
            try {
                if(player.getCurQuest() !=null) {
                    missions = player.getCurQuest().getMissions();
                }
                else{
                    state.getGameServices().getOutput().println("All quests done!");
                }
            } catch (NullPointerException e) {
                e.getSuppressed();
                state.getGameServices().getOutput().println("Missions unavailable");
            }
        }
        if (!missions.isEmpty()) {
            for (Mission m : missions) {
                state.getGameServices().getOutput().println(m.getTask() + " " + ((m.isCompleted()) ? "✔" : "❌"));
            }
        } else {
            state.getGameServices().getOutput().println("No available quest");
        }
    }

    @Override
    public String getDescription() {
        return "progress — displays progress on your current quest and its missions";
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
