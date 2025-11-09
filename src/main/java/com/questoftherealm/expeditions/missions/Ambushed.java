package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.game.Game;

public class Ambushed extends Mission {
    public static boolean playerAmbushed = false;

    public Ambushed(Player player) {
        super("Ambushed!",
                "The goblins discover you—fight your way out!",
                player,
                (p,m)->p.getCurQuest() instanceof GoblinAmbush &&
                        p.getCurQuest().getMissions().get(0).isCompleted() &&
                        p.getCurQuest().getMissions().get(1).isCompleted() && playerAmbushed
        );
    }


}
