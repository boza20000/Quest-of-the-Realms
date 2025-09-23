package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;

public class Warn_the_Castle extends Mission {
    public Warn_the_Castle() {
        super("Warn the Castle", "Return to the capital and inform the king of the looming goblin threat.");
    }

    @Override
    public boolean checkCompletion() {
        return false;
    }
}
