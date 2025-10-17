package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.Gather_Supplies;
import com.questoftherealm.expeditions.missions.Meet_the_Elder;

import java.util.List;

public class StartQuest extends Quest {
    public StartQuest() {
        super("Start Journey",
                List.of(
                        new Meet_the_Elder(),
                        new Gather_Supplies()
                ),
                "Your journey begins. The villagers whisper of dangers lurking in the north."
        );
    }
}
