package com.questoftherealm.map;

import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_General;
import com.questoftherealm.expeditions.missions.Explore_Nearby_Forests;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.Interactions;

import java.util.ArrayList;
import java.util.List;

import static com.questoftherealm.expeditions.missions.Ambushed.playerAmbushed;
import static com.questoftherealm.expeditions.missions.Assemble_an_Army.*;
import static com.questoftherealm.friendlyEntities.Entities.King.hasTalkedToTheKing;

public final class TriggerRegister {
    public static final List<LocationTrigger> triggers = new ArrayList<>();

    static {
        // Village 1
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_1, player -> {
            if (player.getCurQuest() instanceof NorthExploration) {
                Interactions.villageIntro_1();
                Explore_the_Village.setSearched_1(true);
            }
        }));

        // Village 2
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_2, player -> {
            if (player.getCurQuest() instanceof NorthExploration) {
                Interactions.villageIntro_2();
                Explore_the_Village.setSearched_2(true);
            }
        }));

        // Goblin Camp
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            if (player.getCurQuest() instanceof GoblinAmbush) {
                Interactions.goblinCampSpotted();
                Explore_Nearby_Forests.campFound = true;
                player.updateQuestStatus();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Sleep problem");
                    e.getSuppressed();//Make custom exception
                }
                Interactions.goblinsTalkingOverheard();
                playerAmbushed = true;

                player.updateQuestStatus();
                Interactions.makeDecision(Game.getPlayer());
            }
        }));
        // Report findings to Castle
        triggers.add(new LocationTrigger(GameConstants.Castle, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !hasTalkedToTheKing) {
                Interactions.reportToKing();
                reportedToKing = true;
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_1, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !knightsRecruited) {
                Interactions.talkToTheKnights();
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_2, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !archersRecruited) {
                Interactions.talkToTheArchers();
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.MagesOutPost, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && !magesRecruited) {
                Interactions.talkToTheMages();
            }
        }));
        // Army fight
        triggers.add(new LocationTrigger(GameConstants.Battlefield, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat && player.getCurMission().equals(new Defeat_the_Goblin_General())) {
                Interactions.startFinalBattle();
            }
        }));
    }
}
