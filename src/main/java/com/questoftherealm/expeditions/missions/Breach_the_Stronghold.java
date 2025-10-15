package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.Game;

public class Breach_the_Stronghold extends Mission {
    public static boolean isBreached = false;
    public Breach_the_Stronghold() {
        super("Breach the Stronghold", "Fight through endless goblin soldiers to reach the throne room.");
    }
    @Override
    public boolean checkCompletion() {
        if(Game.getPlayer().getCurQuest() instanceof FinalBattle && isBreached){
            complete();
            return true;
        }
        return false;
    }
}
