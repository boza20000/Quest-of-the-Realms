package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;

public class Investigate_Northern_Villages extends Mission {
    public Investigate_Northern_Villages(Player player) {
        super("Investigate northern villages",
                "Search the northern villages for signs of disturbance talk to villagers.",
                player,
                null
        );
    }
    public Investigate_Northern_Villages(){}

    public boolean checkCompletion() {
        if (player.getCurQuest() instanceof NorthExploration q
                && q.isSearchedVillage1()
                && q.isSearchedVillage2()
                && q.isTalkedToVillager1()
                && q.isTalkedToVillager2()) {
            complete();
            return true;
        }
        return false;
    }
}
