package com.questoftherealm.expeditions.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_General;
import com.questoftherealm.expeditions.missions.Warn_the_Castle;

import java.util.List;

public class RiseOfTheGoblinThreat extends Quest {
    private boolean knightsTriedToRecruit = false;
    private boolean archersTriedToRecruit = false;
    private boolean magesTriedToRecruit = false;
    private boolean knightsRecruited = false;
    private boolean archersRecruited = false;
    private boolean magesRecruited = false;
    private boolean reportedToKing = false;
    private int armyPower = 20;
    private boolean isDefeated = false;

    public RiseOfTheGoblinThreat(Player player) {
        super("Rise of the Goblin Threat",
                List.of(
                        new Warn_the_Castle(player),
                        new Assemble_an_Army(player),
                        new Defeat_the_Goblin_General(player)
                ),
                "The goblin forces are preparing for war. Only by uniting the realm can you hope to stand against them.",
                player
        );
    }

    public RiseOfTheGoblinThreat() {
        super("Rise of the Goblin Threat",
                List.of(
                        new Warn_the_Castle(null),
                        new Assemble_an_Army(null),
                        new Defeat_the_Goblin_General(null)
                ),
                "The goblin forces are preparing for war. Only by uniting the realm can you hope to stand against them.",
                null
        );
    }


    public boolean isDefeated() {
        return isDefeated;
    }

    public void setDefeated(boolean defeated) {
        this.isDefeated = defeated;
    }

    public boolean isKnightsTriedToRecruit() {
        return knightsTriedToRecruit;
    }

    public void setKnightsTriedToRecruit(boolean knightsTriedToRecruit) {
        this.knightsTriedToRecruit = knightsTriedToRecruit;
    }

    public boolean isArchersTriedToRecruit() {
        return archersTriedToRecruit;
    }

    public void setArchersTriedToRecruit(boolean archersTriedToRecruit) {
        this.archersTriedToRecruit = archersTriedToRecruit;
    }

    public boolean isMagesTriedToRecruit() {
        return magesTriedToRecruit;
    }

    public void setMagesTriedToRecruit(boolean magesTriedToRecruit) {
        this.magesTriedToRecruit = magesTriedToRecruit;
    }

    public boolean isKnightsRecruited() {
        return knightsRecruited;
    }

    public void setKnightsRecruited(boolean knightsRecruited) {
        this.knightsRecruited = knightsRecruited;
    }

    public boolean isArchersRecruited() {
        return archersRecruited;
    }

    public void setArchersRecruited(boolean archersRecruited) {
        this.archersRecruited = archersRecruited;
    }

    public boolean isMagesRecruited() {
        return magesRecruited;
    }

    public void setMagesRecruited(boolean magesRecruited) {
        this.magesRecruited = magesRecruited;
    }

    public boolean isReportedToKing() {
        return reportedToKing;
    }

    public void setReportedToKing(boolean reportedToKing) {
        this.reportedToKing = reportedToKing;
    }

    public int getArmyPower() {
        return armyPower;
    }

    public void addArmyPower(int armyPower) {
        this.armyPower += armyPower;
    }
}
