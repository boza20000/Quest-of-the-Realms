package com.questoftherealm.game.Commands;

import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.game.Game;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("complete Quest");
    }

    @Override
    public void execute(String[] args) {
        Quest curQuest = Game.getQuests().peek();
        System.out.println("Your current quest: " + curQuest.getName());
        curQuest.listMissions();

    }

    @Override
    public String getDescription() {
        return "-shows the progress on the current quest";
    }
}
