package com.questoftherealm.expeditions.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.Breach_the_Stronghold;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_General;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_King;
import com.questoftherealm.expeditions.missions.March_Into_the_Far_North;

import java.util.List;

public class FinalBattle extends Quest {
    public FinalBattle(Player player) {
        super(
                "Final Battle",
                List.of(
                        new March_Into_the_Far_North(player),
                        new Breach_the_Stronghold(player),
                        new Defeat_the_Goblin_King(player)
                ),
                "The time has come. Deep in the Fifth Kingdom, the Goblin King awaits you.",
                player

        );
    }
}
