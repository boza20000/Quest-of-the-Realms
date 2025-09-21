package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;

import java.util.List;

public class NorthExploration extends Quest {
    public NorthExploration() {
        super("Explore the North",
                List.of(
                        new Mission("Travel North", "Journey through forests and mountains to reach the northern villages."),
                        new Mission("Explore the Village", "Search the northern village for signs of disturbance."),
                        new Mission("Strange Tracks", "You discover goblin tracks leading further north into the wild lands.")
                ),
                "Explore the uncharted north and uncover the source of the growing unease."
        );
    }
}
