package com.questoftherealm.map;

import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.GoblinGeneralManger;
import com.questoftherealm.interaction.GoblinKingManager;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.interaction.RecruitmentManager;
import java.util.ArrayList;
import java.util.List;
import static com.questoftherealm.expeditions.missions.Assemble_an_Army.*;
import static com.questoftherealm.friendlyEntities.Entities.King.hasTalkedToTheKing;

public final class TriggerRegister {
    public static final List<LocationTrigger> triggers = new ArrayList<>();

    static {
        // Village 1
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_1, player -> {
            if (player.getCurQuest() instanceof NorthExploration && player.getCurMission() instanceof Explore_the_Village) {
                MissionInteractions.villageIntro_1();
                Explore_the_Village m = (Explore_the_Village) player.getCurMission();
                m.setSearched_1(true);
            }
        }));

        // Village 2
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_2, player -> {
            if (player.getCurQuest() instanceof NorthExploration && player.getCurMission() instanceof Explore_the_Village) {
                MissionInteractions.villageIntro_2();
                Explore_the_Village m = (Explore_the_Village) player.getCurMission();
                m.setSearched_2(true);
            }
        }));

        // Goblin Camp
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            if (player.getCurQuest() instanceof GoblinAmbush) {
                MissionInteractions.goblinCampSpotted();
                Explore_Nearby_Forests.campFound = true;
                player.updateQuestStatus();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Sleep problem");
                    e.getSuppressed();//Make custom exception
                }
                MissionInteractions.goblinsTalkingOverheard();
                //ambushed true
                player.updateQuestStatus();
                MissionInteractions.makeDecision(Game.getPlayer());
            }
        }));
        // Report findings to Castle
        triggers.add(new LocationTrigger(GameConstants.Castle, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !hasTalkedToTheKing) {
                MissionInteractions.reportToKing();
                reportedToKing = true;
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_1, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !knightsRecruited) {
                RecruitmentManager.talkToTheKnights();
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_2, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !archersRecruited) {
                RecruitmentManager.talkToTheArchers();
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.MagesOutPost, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !magesRecruited) {
                RecruitmentManager.talkToTheMages();
            }
        }));
        // Army fight
        triggers.add(new LocationTrigger(GameConstants.Battlefield, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && player.getCurMission() instanceof Defeat_the_Goblin_General) {
                GoblinGeneralManger.startFinalBattle();
            }
        }));
        triggers.add(new LocationTrigger(GameConstants.FarNorthMountain, player -> {
            if (player.getCurQuest() instanceof FinalBattle && player.getCurMission() instanceof March_Into_the_Far_North){
                GoblinKingManager.goblinKingdomFound();
            }
        }));
    }
}
