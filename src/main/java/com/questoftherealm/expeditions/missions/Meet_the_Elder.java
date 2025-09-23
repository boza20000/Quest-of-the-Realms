package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.FriendlyEntities.Elder;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;

public final class Meet_the_Elder extends Mission {
    public Meet_the_Elder() {
        super("Meet the Elder", "Speak with the village elder about strange rumors from the north.");
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
