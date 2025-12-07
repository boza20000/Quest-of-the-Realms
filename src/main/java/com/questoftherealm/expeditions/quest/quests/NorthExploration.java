package com.questoftherealm.expeditions.quest.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestTypes;
import com.questoftherealm.game.GameState;

public class NorthExploration extends Quest {
    private boolean searchedVillage1 = false;
    private boolean searchedVillage2 = false;
    private boolean talkedToVillager1 = false;
    private boolean talkedToVillager2 = false;

    public NorthExploration(Player player, GameState state) {
        super(QuestTypes.NORTH_EXPLORATION,player,state);
    }
    protected NorthExploration() {
        super();
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
