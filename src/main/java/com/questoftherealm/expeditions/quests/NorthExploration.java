package com.questoftherealm.expeditions.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.*;

import java.util.List;

public class NorthExploration extends Quest {
    public NorthExploration(Player player) {
        super("Explore the North",
                List.of(
                        new Travel_North(player),
                        new Investigate_Northern_Villages(player)
                ),
                "Explore the uncharted north and uncover the source of the growing unease.",
                player
        );
    }
}
