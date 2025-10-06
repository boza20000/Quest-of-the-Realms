package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;

public class Escape_to_Safety extends Mission {
    public static boolean isEscaped = false;
    public Escape_to_Safety(){
        super("Escape to Safety", "Barely escape alive and return with urgent news.");
    }
    @Override
    public boolean checkCompletion() {
        return isEscaped;
    }
}
