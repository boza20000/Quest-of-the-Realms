package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.game.Game;
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
        Player player = Game.getPlayer();
        if (player.getX() == NorthVillage_1_X && player.getY() == NorthVillage_1_Y) {
            isVisited_1 = true;
        }
        if (player.getX() == NorthVillage_2_X && player.getY() == NorthVillage_2_Y) {
            isVisited_2 = true;
        }
        return isVisited_1 && isVisited_2 && isSearched_1 && isSearched_2;
    }

    public static void villageIntro_1(){
        String dialog = """
                From distance you see...the village on fire
                with most houses already burned.
                A few villager spot you and carefully approach ...
                But you don't understand what has happened so you talk to them
                to understand the gravity of the situation.
                """;
        SlowPrinter.slowPrint(dialog);
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

