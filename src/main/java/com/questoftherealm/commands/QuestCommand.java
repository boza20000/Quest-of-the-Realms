package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.game.GameState;

public class QuestCommand extends Command {

    public QuestCommand() {
        super("quest");
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("quest.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("quest.usage", getDescription(state)));
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
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("quest.error.noQuestLoaded"));
            return;
        }
        if (player.getCurMission() == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("quest.error.noMissionLoaded"));
            return;
        }
        if (player.getQuestFactory().getCurrentQuest() == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("quest.error.noQuestLoaded"));
        }

        for (Mission m : player.getQuestFactory().getCurrentQuest().getMissions()) {
            state.getGameServices().getOutput().println(m.getTask());
        }
    }

}