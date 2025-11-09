package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.Game;

public class Talk_To_Survivors extends Mission {

    public Talk_To_Survivors(Player player) {
        super("Survivors",
                "Talk to the survivors from the north village.",
                player,
                (p, m) -> (p.getCurQuest() instanceof NorthExploration)
                        && Game.getGameMap().curZone(p.getX(), p.getY()).getNpcByType(NpcType.Villager).isHasTalked()

        );
    }
}
