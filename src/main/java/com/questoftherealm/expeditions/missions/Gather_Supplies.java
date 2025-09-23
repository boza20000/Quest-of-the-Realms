package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;

public final class Gather_Supplies extends Mission {
    public Gather_Supplies() {
        super("Gather Supplies", "Collect weapons and food for the journey ahead.");
    }
    @Override
    public boolean checkCompletion() {
        return false;
    }
}
