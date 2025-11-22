package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.GoblinAmbush;

public class Ambushed extends Mission {
    public Ambushed(Player player) {
        super("Ambushed!",
                "The goblins discover you—fight your way out!",
                player,
                (p,m)->(p.getCurQuest() instanceof GoblinAmbush q && q.isPlayerAmbushed() && q.isCampFound()
        ));
    }
    public Ambushed(){}
}
