package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.Position;
import static com.questoftherealm.game.GameConstants.*;

public class Explore_the_Village extends Mission {
    private boolean isSearched_1 = false;
    private boolean isSearched_2 = false;
    private boolean isVisited_1 = false;
    private boolean isVisited_2 = false;

    public Explore_the_Village(Player player) {
        super("Explore the Village", "Search the northern villages for signs of disturbance.",player);
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;

        Position pos = new Position(player.getX(),player.getY());
        if (player.getCurQuest() instanceof NorthExploration && pos.equals(NorthVillage_1)) {
            isVisited_1 = true;
        }
        if (player.getCurQuest() instanceof NorthExploration && pos.equals(NorthVillage_2)) {
            isVisited_2 = true;
        }
        if(player.getCurQuest() instanceof NorthExploration && isVisited_1 && isVisited_2 && isSearched_1 && isSearched_2){
            complete();
            return true;
        }
        return false;
    }

    public void setSearched_1(boolean searched_1) {
        isSearched_1 = searched_1;
    }

    public  void setSearched_2(boolean searched_2) {
        isSearched_2 = searched_2;
    }

    public boolean isSearched_1() {
        return isSearched_1;
    }

    public boolean isSearched_2() {
        return isSearched_2;
    }
}

