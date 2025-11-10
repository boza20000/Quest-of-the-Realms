package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;

public class Investigate_Northern_Villages extends Mission {
    private boolean searchedVillage1;
    private boolean searchedVillage2;
    private boolean talkedToVillager1;
    private boolean talkedToVillager2;

    public Investigate_Northern_Villages(Player player) {
        super("Investigate northern villages",
                "Search the northern villages for signs of disturbance talk to villagers.",
                player,
                null
        );
    }

    public boolean checkCompletion() {
        if (player.getCurQuest() instanceof NorthExploration
                && searchedVillage1
                && searchedVillage2
                && talkedToVillager1
                && talkedToVillager2) {
            complete();
            return true;
        }
        return false;
    }

    public void setSearchedVillage1(boolean v) { searchedVillage1 = v; }
    public void setSearchedVillage2(boolean v) { searchedVillage2 = v; }
    public void setTalkedToVillager1(boolean v) { talkedToVillager1 = v; }
    public void setTalkedToVillager2(boolean v) { talkedToVillager2 = v; }

    public boolean isSearchedVillage1() { return searchedVillage1; }
    public boolean isSearchedVillage2() { return searchedVillage2; }
    public boolean isTalkedToVillager1() { return talkedToVillager1; }
    public boolean isTalkedToVillager2() { return talkedToVillager2; }
}
