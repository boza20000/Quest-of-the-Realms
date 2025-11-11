package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.game.Game;

public class Escape_to_Safety extends Mission {
    public Escape_to_Safety(Player player) {
        super("Escape to Safety",
                "Barely escape alive and return with urgent news.",
                player,
                (p, m) -> player.getCurQuest() instanceof GoblinAmbush q && q.isPlayerEscapedAmbush()
        );
    }
    public Escape_to_Safety(){}
}
