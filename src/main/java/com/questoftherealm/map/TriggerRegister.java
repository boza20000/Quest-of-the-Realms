package com.questoftherealm.map;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Explore_Nearby_Forests;
import com.questoftherealm.expeditions.missions.Infiltrate_the_Camp;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.Interactions;

import java.util.ArrayList;
import java.util.List;

public final class TriggerRegister {
    public static final List<LocationTrigger> triggers = new ArrayList<>();

    static {
        // Village 1
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_1, player -> {
            Interactions.villageIntro_1();
            Explore_the_Village.setSearched_1(true);
        }));

        // Village 2
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_2, player -> {
            Interactions.villageIntro_2();
            Explore_the_Village.setSearched_2(true);
        }));

        // Goblin Camp
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            Interactions.goblinCampSpotted();
            Explore_Nearby_Forests.campFound = true;
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e){
                System.out.println("Sleep problem");
                e.getSuppressed();//Make custom exception
            }
            Interactions.goblinsTalkingOverheard();
            Interactions.makeDecision(Game.getPlayer());
        }));



    }
}
