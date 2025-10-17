package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.expeditions.missions.Meet_the_Elder;
import com.questoftherealm.expeditions.missions.Talk_To_Survivors;
import com.questoftherealm.expeditions.missions.Travel_North;

import java.util.List;

public class NorthExploration extends Quest {
    public NorthExploration() {
        super("Explore the North",
                List.of(
                        new Travel_North(),
                        new Explore_the_Village(),
                        new Talk_To_Survivors()
                ),
                "Explore the uncharted north and uncover the source of the growing unease."
        );
    }
}
