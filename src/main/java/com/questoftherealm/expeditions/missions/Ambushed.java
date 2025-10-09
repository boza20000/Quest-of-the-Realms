package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.game.Game;

public class Ambushed extends Mission {
    public static boolean playerAmbushed = false;

    public Ambushed() {
        super("Ambushed!", "The goblins discover you—fight your way out!");
    }


    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        Quest cur = Game.getPlayer().getCurQuest();
        if (cur instanceof GoblinAmbush &&
                cur.getMissions().get(0).isCompleted() &&
                cur.getMissions().get(1).isCompleted() && playerAmbushed) {
            complete();
            return true;
        }
        return false;
    }
}
