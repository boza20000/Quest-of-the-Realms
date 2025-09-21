package com.questoftherealm.expeditions.quests;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;

import java.util.List;

public class RiseOfTheGoblinThreat extends Quest {
    public RiseOfTheGoblinThreat() {
        super("Rise of the Goblin Threat",
                List.of(
                        new Mission("Warn the Castle", "Return to the capital and inform the king of the looming goblin threat."),
                        new Mission("Assemble an Army", "Rally knights, archers, and mages to face the goblins."),
                        new Mission("Defeat the Goblin General", "Lead your army to victory by slaying the Goblin General in battle.")
                ),
                "The goblin forces are preparing for war. Only by uniting the realm can you hope to stand against them."
        );
    }
}
