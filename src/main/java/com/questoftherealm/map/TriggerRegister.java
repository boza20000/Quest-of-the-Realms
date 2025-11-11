package com.questoftherealm.map;

import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.GoblinGeneralManger;
import com.questoftherealm.interaction.GoblinKingManager;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.interaction.RecruitmentManager;

import java.util.ArrayList;
import java.util.List;

public final class TriggerRegister {
    public static final List<LocationTrigger> triggers = new ArrayList<>();
    public static RecruitmentManager recruitmentManager = new RecruitmentManager();
    public static MissionInteractions missionInteractions = new MissionInteractions();
    public static GoblinGeneralManger goblinGeneralManger = new GoblinGeneralManger();
    public static GoblinKingManager goblinKingManager = new GoblinKingManager();

    static {
        // Village 1
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_1, player -> {
            if (player.getCurQuest() instanceof NorthExploration q && player.getCurMission() instanceof Investigate_Northern_Villages) {
                missionInteractions.villageIntro_1();
                q.setSearchedVillage1(true);
            }
        }));

        // Village 2
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_2, player -> {
            if (player.getCurQuest() instanceof NorthExploration q && player.getCurMission() instanceof Investigate_Northern_Villages) {
                missionInteractions.villageIntro_2();
                q.setSearchedVillage2(true);
            }
        }));

        // Goblin Camp
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            if (player.getCurQuest() instanceof GoblinAmbush q) {
                if (player.getCurMission() instanceof Explore_Nearby_Forests) {
                    missionInteractions.goblinCampSpotted();
                    q.setCampFound(true);
                    player.updateQuestStatus();
                }
                pause();
                if (player.getCurMission() instanceof Ambushed) {
                    missionInteractions.goblinsTalkingOverheard();
                    q.setPlayerAmbushed(true);
                    player.updateQuestStatus();
                }
                if (player.getCurMission() instanceof Escape_to_Safety) {
                    missionInteractions.makeDecision(player);
                    player.updateQuestStatus();
                }

            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_1, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isKnightsRecruited()) {
                recruitmentManager.talkToTheKnights(player);

            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_2, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isArchersRecruited()) {
                recruitmentManager.talkToTheArchers(player);
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.MagesOutPost, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isMagesRecruited()) {
                recruitmentManager.talkToTheMages(player);
            }
        }));
        // Army fight
        triggers.add(new LocationTrigger(GameConstants.Battlefield, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && player.getCurMission() instanceof Defeat_the_Goblin_General) {
                goblinGeneralManger.startFinalBattle(player, q);
            }
        }));
        triggers.add(new LocationTrigger(GameConstants.FarNorthMountain, player -> {
            if (player.getCurQuest() instanceof FinalBattle q && player.getCurMission() instanceof March_Into_the_Far_North) {
                goblinKingManager.goblinKingdomFound(player, q);
            }
        }));
    }

    static void pause() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Sleep problem");
            e.getSuppressed();//Make custom exception
        }
    }
}
