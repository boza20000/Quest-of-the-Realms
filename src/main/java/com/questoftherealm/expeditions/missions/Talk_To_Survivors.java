package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.Game;
import com.questoftherealm.map.Tile;

public class Talk_To_Survivors extends Mission {

    public Talk_To_Survivors(Player player) {
        super("Survivors",
                "Talk to the survivors from the north village.",
                player,
                (p, m) -> (player.getCurQuest() instanceof NorthExploration)
                        && Game.getGameMap().curZone(player.getX(), player.getY()).getNpcByType(NpcType.Villager).isHasTalked()

        );
    }
}
