package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;

public class Assemble_an_Army extends Mission {
    public Assemble_an_Army() {
        super("Assemble an Army", "Rally knights, archers, and mages to face the goblins.");
    }
    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if(Game.getPlayer().getCurQuest() instanceof RiseOfTheGoblinThreat){
            return true;
        }
        return false;
    }
}
