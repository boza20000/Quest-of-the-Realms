package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;

public class Strange_Tracks extends Mission {
    public Strange_Tracks(Player player) {
        super("Strange Tracks", "You discover goblin tracks leading further north into the wild lands.",player);
    }
    @Override
    public boolean checkCompletion() {
        return false;
    }
}
