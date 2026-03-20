package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.MessageBundle;

import java.util.List;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("progress");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        MessageBundle bundle = state.getMessages().getBundle();
        Output output = state.getGameServices().getOutput();
        if (player.getCurQuest() == null) {
            output.println(bundle.get("progress.error.noQuestLoaded"));
            return;
        }
        if (player.getCurMission() == null) {
            output.println(bundle.get("progress.error.noMissionLoaded"));
            return;
        }
        List<Mission> missions = List.of();
        if (player.getCurQuest() != null) {
            missions = player.getCurQuest().getMissions();
        }
        if (!missions.isEmpty()) {
            String completedSymbol = "✔";
            String incompleteSymbol = "❌";
            for (Mission m : missions) {
                output.println(m.getTask() + " " + ((m.isCompleted()) ? completedSymbol : incompleteSymbol));
            }
        } else {
            output.println(bundle.get("progress.info.noAvailableQuest"));
        }
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("progress.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("progress.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player, state);
    }

}