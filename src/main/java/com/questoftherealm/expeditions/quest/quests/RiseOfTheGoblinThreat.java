package com.questoftherealm.expeditions.quest.quests;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestTypes;
import com.questoftherealm.game.GameState;

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

    public RiseOfTheGoblinThreat(Player player, GameState state) {
        super(QuestTypes.RISE_OF_THE_GOBLIN_THREAT,player,state);
    }

    protected RiseOfTheGoblinThreat() {
        super();
    }

    public void updateArmyStatus() {
        if (this.isKnightsRecruited()) {
            this.addArmyPower(15);
        }
        if (this.isMagesRecruited()) {
            this.addArmyPower(20);
        }
        if (this.isArchersRecruited()) {
            this.addArmyPower(20);
        }
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
