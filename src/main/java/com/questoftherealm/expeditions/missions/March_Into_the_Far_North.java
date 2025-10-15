package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;

public class March_Into_the_Far_North extends Mission {
    public March_Into_the_Far_North() {
        super("March Into the Far North Mountains", "Lead your army into the frozen lands of the secret Fifth Kingdom.");
    }

    @Override
    public boolean checkCompletion() {
        //&& Game.getPlayer().getY() == GameConstants.FarNorth
        if (Game.getPlayer().getCurQuest() instanceof FinalBattle Y) {
            complete();
            return true;
        }
        return false;
    }
}
