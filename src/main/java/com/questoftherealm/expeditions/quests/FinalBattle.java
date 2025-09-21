package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;

import java.util.List;

public class FinalBattle extends Quest {
    public FinalBattle() {
        super(
                "Final Battle",
                List.of(
                        new Mission("March Into the Far North", "Lead your army into the frozen lands of the secret Fifth Kingdom."),
                        new Mission("Breach the Stronghold", "Fight through endless goblin soldiers to reach the throne room."),
                        new Mission("Defeat the Goblin King", "Face the Goblin King in a final duel to end the goblin threat forever.")
                ),
                "The time has come. Deep in the Fifth Kingdom, the Goblin King awaits you."
        );
    }
}
