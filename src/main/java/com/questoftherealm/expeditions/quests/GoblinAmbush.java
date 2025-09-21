package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;

import java.util.List;

public class GoblinAmbush extends Quest {
    public GoblinAmbush() {
        super(
                "Goblin Ambush",
                List.of(
                        new Mission("Infiltrate the Camp", "Sneak into a goblin camp to learn their plans."),
                        new Mission("Ambushed!", "The goblins discover you—fight your way out!"),
                        new Mission("Escape to Safety", "Barely escape alive and return with urgent news.")
                ),
                "You stumble upon a goblin war camp, but your presence does not go unnoticed..."
        );
    }
}
