package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;

public class Defeat_the_Goblin_General extends Mission {
    public static boolean isDefeated = false;
    public Defeat_the_Goblin_General(Player player) {
        super("Defeat the Goblin General", "Lead your army to victory by slaying the Goblin General in battle.",player);
    }
    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if(player.getCurQuest() instanceof RiseOfTheGoblinThreat && isDefeated){
            complete();
            return true;
        }
        return false;
    }
}
