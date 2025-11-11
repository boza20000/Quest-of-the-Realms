package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;

public class Defeat_the_Goblin_General extends Mission {

    public Defeat_the_Goblin_General(Player player) {
        super("Defeat the Goblin General",
                "Lead your army to victory by slaying the Goblin General in battle.",
                player,
                (p, m) -> p.getCurQuest() instanceof RiseOfTheGoblinThreat q && q.isDefeated()
        );
    }
    public Defeat_the_Goblin_General(){}
}
