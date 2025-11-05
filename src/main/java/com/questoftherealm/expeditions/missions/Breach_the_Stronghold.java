package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.Game;
import com.questoftherealm.characters.player.Player;

public class Breach_the_Stronghold extends Mission {
    public static boolean isBreached = false;
    public Breach_the_Stronghold(Player player) {
        super("Breach the Stronghold", "Fight through endless goblin soldiers to reach the throne room.",player);
    }
    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if(player.getCurQuest() instanceof FinalBattle && isBreached){
            complete();
            return true;
        }
        return false;
    }
}
