package com.questoftherealm.commands;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;

import java.util.List;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("complete Quest");
    }

    @Override
    public void execute(String[] args) {
        List<Mission> missions = Game.getQuests().peek().getMissions();
        if (!missions.isEmpty()) {
            for (Mission m : missions) {
                System.out.println(m.getTask() + " " + ((m.isCompleted()) ? "✔" : "❌"));
            }
        }
    }

    @Override
    public String getDescription() {
        return "-shows the progress on the current quest";
    }
}
