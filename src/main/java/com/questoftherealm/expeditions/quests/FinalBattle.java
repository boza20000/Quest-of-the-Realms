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
    private boolean isBreached = false;
    private boolean isDefeated = false;

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

    public FinalBattle() {
        super(
                "Final Battle",
                List.of(
                        new March_Into_the_Far_North(null),
                        new Breach_the_Stronghold(null),
                        new Defeat_the_Goblin_King(null)
                ),
                "The time has come. Deep in the Fifth Kingdom, the Goblin King awaits you.",
                null

        );
    }

    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        isDefeated = defeated;
    }

    public boolean isBreached() {
        return isBreached;
    }

    public void setBreached(boolean breached) {
        isBreached = breached;
    }
}
