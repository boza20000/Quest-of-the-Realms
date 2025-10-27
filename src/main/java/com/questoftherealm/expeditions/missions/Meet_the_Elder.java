package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;

public final class Meet_the_Elder extends Mission {
    public Meet_the_Elder() {
        super("Meet the Elder", "Speak with the castle elder about strange rumors from the north.");
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if (Game.getPlayer().getCurQuest() instanceof StartQuest && !isCompleted() && Elder.isHasTalked()) {
            complete();
            return true;
        }
        return false;
    }

}
