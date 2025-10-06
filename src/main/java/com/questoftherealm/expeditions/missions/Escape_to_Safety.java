package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;

public class Escape_to_Safety extends Mission {
    public static boolean playerEscapedAmbush = false;

    public Escape_to_Safety() {
        super("Escape to Safety", "Barely escape alive and return with urgent news.");
    }

    @Override
    public boolean checkCompletion() {
        if (playerEscapedAmbush) {
            complete();
            return true;
        }
        return false;
    }
}
