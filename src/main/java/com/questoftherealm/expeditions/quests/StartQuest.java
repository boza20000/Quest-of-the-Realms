package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;

import java.util.List;

public class StartQuest extends Quest {
    public StartQuest() {
        super("Start Journey",
                List.of(
                        new Mission("Meet the Elder", "Speak with the village elder about strange rumors from the north."),
                        new Mission("Gather Supplies", "Collect weapons and food for the journey ahead.")
                ),
                "Your journey begins. The villagers whisper of dangers lurking in the north."
        );
    }
}
