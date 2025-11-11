package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.FinalBattle;

public class Defeat_the_Goblin_King extends Mission {

    public Defeat_the_Goblin_King(Player player) {
        super("Defeat the Goblin King",
                "Face the Goblin King in a final duel to end the goblin threat forever.",
                player,
                (p,m)->(player.getCurQuest() instanceof FinalBattle q) && q.isDefeated()
                );
    }
    public Defeat_the_Goblin_King(){}
}
