package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.game.GameConstants;

public class Travel_North extends Mission {
    public Travel_North(Player player) {
        super("Travel North",
                "Journey through forests and mountains to reach the northern region.",
                player,
                (p, m) -> (p.getCurQuest() instanceof NorthExploration) && p.getY() <= GameConstants.North_Y
        );
    }
    public Travel_North(){}
}
