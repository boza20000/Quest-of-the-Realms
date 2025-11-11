package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.Game;
import com.questoftherealm.characters.player.Player;

public class Breach_the_Stronghold extends Mission {

    public Breach_the_Stronghold(Player player) {
        super("Breach the Stronghold",
                "Fight through endless goblin soldiers to reach the throne room.",
                player,
                (p, m) -> p.getCurQuest() instanceof FinalBattle q && q.isBreached()
        );
    }
    public Breach_the_Stronghold(){}
}
