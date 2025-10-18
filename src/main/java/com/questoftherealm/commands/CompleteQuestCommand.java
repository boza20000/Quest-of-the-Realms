package com.questoftherealm.commands;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;

import java.util.List;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("progress Quest");
    }

    @Override
    public void execute(String[] args) {
        List<Mission> missions = List.of();
        if(Game.getPlayer().getCurQuest()!=null) {
            try {
                missions = Game.getQuests().peek().getMissions();
            } catch (NullPointerException e) {
                e.getSuppressed();
                System.out.println("Missions unavailable they are null");
            }
        }
        if (!missions.isEmpty()) {
            for (Mission m : missions) {
                System.out.println(m.getTask() + " " + ((m.isCompleted()) ? "✔" : "❌"));
            }
        }
        else{
            System.out.println("No available quest");
        }
    }

    @Override
    public String getDescription() {
        return "-shows the progress on the current quest";
    }
}
