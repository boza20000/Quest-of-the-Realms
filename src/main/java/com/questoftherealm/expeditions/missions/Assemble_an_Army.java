package com.questoftherealm.expeditions.missions;

import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;

public class Assemble_an_Army extends Mission {
    public static boolean knightsTriedToRecruit = false;
    public static boolean archersTriedToRecruit = false;
    public static boolean magesTriedToRecruit = false;
    public static boolean knightsRecruited = false;
    public static boolean archersRecruited = false;
    public static boolean magesRecruited = false;
    public static boolean reportedToKing = false;
    public static int armyPower = 20;

    public Assemble_an_Army() {
        super("Assemble an Army", "Rally knights, archers, and mages to face the goblins.");
    }
    @Override
    public boolean checkCompletion() {
        if(isCompleted())return true;
        if(Game.getPlayer().getCurQuest() instanceof RiseOfTheGoblinThreat &&
                knightsTriedToRecruit && archersTriedToRecruit && magesTriedToRecruit && reportedToKing){
            updateArmyStatus();
            complete();
            return true;
        }
        return false;
    }

    public static void updateArmyStatus() {
        if(knightsRecruited){
            armyPower+=15;
        }
        if(magesRecruited){
            armyPower+=20;
        }
        if(archersRecruited){
            armyPower+=15;
        }
    }
}
