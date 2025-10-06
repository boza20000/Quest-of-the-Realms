package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.*;

import java.util.List;

public class GoblinAmbush extends Quest {
    public GoblinAmbush() {
        super(
                "Goblin Ambush",
                List.of(
                        new Explore_Nearby_Forests(),
                        new Infiltrate_the_Camp(),
                        new Ambushed(),
                        new Escape_to_Safety()
                ),
                "You stumble upon a goblin war camp, but your presence does not go unnoticed..."
        );
    }
}
