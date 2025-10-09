package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;

public class Warn_the_Castle extends Mission {
    public Warn_the_Castle() {
        super("Warn the Castle", "Return to the capital and inform the king of the looming goblin threat.");
    }

    @Override
    public boolean checkCompletion() {
        if(Game.getPlayer().getCurQuest() instanceof RiseOfTheGoblinThreat){
            return false;
        }
        return false;
    }
}
