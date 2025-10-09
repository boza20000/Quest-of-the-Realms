package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.Position;
import com.questoftherealm.interaction.SlowPrinter;

import static com.questoftherealm.game.GameConstants.*;

public class Explore_the_Village extends Mission {
    private static boolean isSearched_1 = false;
    private static boolean isSearched_2 = false;
    private boolean isVisited_1 = false;
    private boolean isVisited_2 = false;

    public Explore_the_Village() {
        super("Explore the Village", "Search the northern villages for signs of disturbance.");
    }

    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;

        Player player = Game.getPlayer();
        Position pos = new Position(player.getX(),player.getY());
        if (Game.getPlayer().getCurQuest() instanceof NorthExploration && pos.equals(NorthVillage_1)) {
            isVisited_1 = true;
        }
        if (Game.getPlayer().getCurQuest() instanceof NorthExploration && pos.equals(NorthVillage_2)) {
            isVisited_2 = true;
        }
        if(Game.getPlayer().getCurQuest() instanceof NorthExploration && isVisited_1 && isVisited_2 && isSearched_1 && isSearched_2){
            complete();
            return true;
        }
        return false;
    }

    public static void setSearched_1(boolean searched_1) {
        isSearched_1 = searched_1;
    }

    public static void setSearched_2(boolean searched_2) {
        isSearched_2 = searched_2;
    }

    public static boolean isHasSearched_1() {
        return isSearched_1;
    }

    public static boolean isHasSearched_2() {
        return isSearched_2;
    }
}

