package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.game.Game;

public class Talk_To_Survivors extends Mission {

    public Talk_To_Survivors() {
        super("Survivors", "Talk to the survivors from the north village.");
    }

    @Override
    public boolean checkCompletion() {
        return Game.getPlayer().getCurQuest() instanceof NorthExploration && Villager.hasTalkedToAll();
    }
}
