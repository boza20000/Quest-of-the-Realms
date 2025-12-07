package com.questoftherealm.map.interfaces;

import com.questoftherealm.characters.player.Player;

public interface TriggerCondition {
    boolean matches(Player p);
}
