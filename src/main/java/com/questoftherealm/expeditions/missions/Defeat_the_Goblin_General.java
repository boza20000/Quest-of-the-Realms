package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;

public class Defeat_the_Goblin_General extends Mission {
    public static boolean isDefeated = false;
    public Defeat_the_Goblin_General() {
        super("Defeat the Goblin General", "Lead your army to victory by slaying the Goblin General in battle.");
    }
    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if(Game.getPlayer().getCurQuest() instanceof RiseOfTheGoblinThreat && isDefeated){
            complete();
            return true;
        }
        return false;
    }
}
