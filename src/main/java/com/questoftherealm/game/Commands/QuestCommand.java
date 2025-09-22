package com.questoftherealm.game.Commands;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.QuestFactory;

public class QuestCommand extends Command {

    public QuestCommand() {
        super("quest");
    }

    @Override
    public String getDescription() {
        return "gives a list of all missions needed to complete the current quest";
    }

    @Override
    public void execute(String[] args) {
        for(Mission m: QuestFactory.getCurrentQuest().getMissions()){
            System.out.println(m.getTask());
        }
    }

}
