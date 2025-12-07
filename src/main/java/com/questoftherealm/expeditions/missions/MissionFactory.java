package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;

public class MissionFactory {
    public static Mission createMission(Missions type, Player player, GameState state) {
        return new Mission(type,player,state);
    }

}
