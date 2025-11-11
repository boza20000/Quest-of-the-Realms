package com.questoftherealm.expeditions.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.*;

import java.util.List;

public class NorthExploration extends Quest {
    private boolean searchedVillage1 = false;
    private boolean searchedVillage2 = false;
    private boolean talkedToVillager1 = false;
    private boolean talkedToVillager2 = false;

    public NorthExploration(Player player) {
        super("Explore the North",
                List.of(
                        new Travel_North(player),
                        new Investigate_Northern_Villages(player)
                ),
                "Explore the uncharted north and uncover the source of the growing unease.",
                player
        );
    }
    public NorthExploration() {
        super("Explore the North",
                List.of(
                        new Travel_North(null),
                        new Investigate_Northern_Villages(null)
                ),
                "Explore the uncharted north and uncover the source of the growing unease.",
                null
        );
    }

    public void setSearchedVillage1(boolean v) {
        searchedVillage1 = v;
    }

    public void setSearchedVillage2(boolean v) {
        searchedVillage2 = v;
    }

    public void setTalkedToVillager1(boolean v) {
        talkedToVillager1 = v;
    }

    public void setTalkedToVillager2(boolean v) {
        talkedToVillager2 = v;
    }

    public boolean isSearchedVillage1() {
        return searchedVillage1;
    }

    public boolean isSearchedVillage2() {
        return searchedVillage2;
    }

    public boolean isTalkedToVillager1() {
        return talkedToVillager1;
    }

    public boolean isTalkedToVillager2() {
        return talkedToVillager2;
    }
}
