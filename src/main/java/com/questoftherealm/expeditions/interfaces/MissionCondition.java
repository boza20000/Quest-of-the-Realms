package com.questoftherealm.expeditions.interfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;

public interface MissionCondition {
    boolean check(Player player, Mission mission);

}
