package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;

public class Defeat_the_Goblin_King extends Mission {
    public Defeat_the_Goblin_King() {
        super("Defeat the Goblin King", "Face the Goblin King in a final duel to end the goblin threat forever.");
    }
    @Override
    public boolean checkCompletion() {
        return false;
    }
}
