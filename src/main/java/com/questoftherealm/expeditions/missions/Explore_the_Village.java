package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.game.Game;

import static com.questoftherealm.game.GameConstants.*;

public class Explore_the_Village extends Mission {
    public Explore_the_Village() {
        super("Explore the Village", "Search the northern villages for signs of disturbance.");
    }

    @Override
    public boolean checkCompletion() {
        boolean isVisited_1 = false;
        boolean isVisited_2 = false;
        Player player = Game.getPlayer();
        if (player.getX() == NorthVillage_1_X && player.getX() == NorthVillage_1_Y) {
            isVisited_1 = true;
        }
        if (player.getX() == NorthVillage_2_X && player.getX() == NorthVillage_2_Y) {
            isVisited_2 = true;
        }
        return isVisited_1 && isVisited_2 && Villager.isHasTalked();
    }
}
