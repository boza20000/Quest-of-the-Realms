package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.GameState;

public class QuestCommand extends Command {

    public QuestCommand() {
        super("quest");
    }

    @Override
    public String getDescription(GameState state) {
        return "quest — shows your active quest and its list of missions";
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println("Usage: " + getDescription(state));
            return false;
        }
        return playerBaseCheck(player,state);
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
        if (player.getQuestFactory().getCurrentQuest() == null) {
            state.getGameServices().getOutput().println("Error: No quest loaded.");
        }

        for (Mission m : player.getQuestFactory().getCurrentQuest().getMissions()) {
            state.getGameServices().getOutput().println(m.getTask());
        }
    }

}
