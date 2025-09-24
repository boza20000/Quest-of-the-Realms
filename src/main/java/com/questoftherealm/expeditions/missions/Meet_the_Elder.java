package com.questoftherealm.expeditions.missions;

import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.expeditions.Mission;

public final class Meet_the_Elder extends Mission {
    public Meet_the_Elder() {
        super("Meet the Elder", "Speak with the castle elder about strange rumors from the north.");
    }

    @Override
    public boolean checkCompletion() {
        if (!isCompleted() && Elder.isHasTalked()) {
            complete();
            return true;
        }
        return isCompleted();
    }

}
