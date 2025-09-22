package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.Ambushed;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_General;
import com.questoftherealm.expeditions.missions.Escape_to_Safety;

import java.util.List;

public class GoblinAmbush extends Quest {
    public GoblinAmbush() {
        super(
                "Goblin Ambush",
                List.of(
                        new Ambushed(),
                        new Defeat_the_Goblin_General(),
                        new Escape_to_Safety()
                ),
                "You stumble upon a goblin war camp, but your presence does not go unnoticed..."
        );
    }
}
