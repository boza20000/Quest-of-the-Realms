package com.questoftherealm.expeditions.missions.interfaces;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Mission;

@FunctionalInterface
public interface MissionCondition {
    boolean check(Player player, Mission mission);

}
