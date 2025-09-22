package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_General;
import com.questoftherealm.expeditions.missions.Warn_the_Castle;

import java.util.List;

public class RiseOfTheGoblinThreat extends Quest {
    public RiseOfTheGoblinThreat() {
        super("Rise of the Goblin Threat",
                List.of(
                        new Warn_the_Castle(),
                        new Assemble_an_Army(),
                        new Defeat_the_Goblin_General()
                ),
                "The goblin forces are preparing for war. Only by uniting the realm can you hope to stand against them."
        );
    }
}
