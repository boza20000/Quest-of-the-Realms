package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.FinalBattle;

public class Defeat_the_Goblin_King extends Mission {
    public static boolean isDefeated = false;
    public Defeat_the_Goblin_King(Player player) {
        super("Defeat the Goblin King", "Face the Goblin King in a final duel to end the goblin threat forever.",player);
    }
    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if((player.getCurQuest() instanceof FinalBattle) && isDefeated) {
            complete();
            return true;
        }
        return false;
    }
}
