package com.questoftherealm.expeditions.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.*;

import java.util.List;

public class GoblinAmbush extends Quest {
    public GoblinAmbush(Player player) {
        super(
                "Goblin Ambush",
                List.of(
                        new Explore_Nearby_Forests(player),
                        new Infiltrate_the_Camp(player),
                        new Ambushed(player),
                        new Escape_to_Safety(player)
                ),
                "You stumble upon a goblin war camp, but your presence does not go unnoticed...",
                player
        );
    }
}
