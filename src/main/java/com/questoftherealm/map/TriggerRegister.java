package com.questoftherealm.map;

import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.GoblinGeneralManager;
import com.questoftherealm.interaction.GoblinKingManager;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.interaction.RecruitmentManager;

import java.util.ArrayList;
import java.util.List;

public final class TriggerRegister {
    private final List<LocationTrigger> triggers = new ArrayList<>();
    private final RecruitmentManager recruitmentManager;
    private final MissionInteractions missionInteractions;
    private final GoblinGeneralManager goblinGeneralManager;
    private final GoblinKingManager goblinKingManager;
    private final GameState state;
    private boolean isExecuted = false;

    public TriggerRegister(GameState state) {
        registerTriggers();
        this.state = state;
        this.recruitmentManager = new RecruitmentManager(state);
        this.missionInteractions = new MissionInteractions(state);
        this.goblinGeneralManager = new GoblinGeneralManager(state);
        this.goblinKingManager = new GoblinKingManager(state);
    }

    public List<LocationTrigger> getTriggers() {
        return triggers;
    }

    private void registerTriggers() {
        registerVillageTriggers();
        registerGoblinCampTriggers();
        registerAssemblyArmyTriggers();
        registerArmyFightTriggers();
    }

    private void registerVillageTriggers() {
        // Village 1
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_1, player -> {
            if (player.getCurQuest() instanceof NorthExploration q && player.getCurMission() instanceof Investigate_Northern_Villages) {
                missionInteractions.villageIntro_1();
                q.setSearchedVillage1(true);
                isExecuted = true;
            }
        }));

        // Village 2
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_2, player -> {
            if (player.getCurQuest() instanceof NorthExploration q && player.getCurMission() instanceof Investigate_Northern_Villages) {
                missionInteractions.villageIntro_2();
                q.setSearchedVillage2(true);
                isExecuted = true;
            }
        }));
    }

    private void registerGoblinCampTriggers() {
        // Goblin Camp
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            if (player.getCurQuest() instanceof GoblinAmbush q) {
                if (player.getCurMission() instanceof Explore_Nearby_Forests) {
                    missionInteractions.goblinCampSpotted();
                    q.setCampFound(true);
                    player.updateQuestStatus(state);
                    isExecuted = true;
                }
                pause(state.getGameServices().getOutput());
                if (player.getCurMission() instanceof Ambushed) {
                    missionInteractions.goblinsTalkingOverheard();
                    q.setPlayerAmbushed(true);
                    player.updateQuestStatus(state);
                    isExecuted = true;
                }
                if (player.getCurMission() instanceof Escape_to_Safety) {
                    missionInteractions.makeDecision(player);
                    player.updateQuestStatus(state);
                    isExecuted = true;
                }

            }
        }));
    }

    private void registerAssemblyArmyTriggers() {
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_1, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isKnightsRecruited()) {
                recruitmentManager.talkToTheKnights(player, state);
                isExecuted = true;
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_2, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isArchersRecruited()) {
                recruitmentManager.talkToTheArchers(player, state);
                isExecuted = true;
            }
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.MagesOutPost, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && !q.isMagesRecruited()) {
                recruitmentManager.talkToTheMages(player, state);
                isExecuted = true;
            }
        }));
    }

    private void registerArmyFightTriggers() {
        // Army fight
        triggers.add(new LocationTrigger(GameConstants.Battlefield, player -> {
            if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && player.getCurMission() instanceof Defeat_the_Goblin_General) {
                goblinGeneralManager.startFinalBattle(player, q, state);
                isExecuted = true;
            }
        }));
        //Goblin Cave
        triggers.add(new LocationTrigger(GameConstants.FarNorthMountain, player -> {
            if (player.getCurQuest() instanceof FinalBattle q && player.getCurMission() instanceof March_Into_the_Far_North) {
                goblinKingManager.goblinKingdomFound(player, q);
                isExecuted = true;
            }
        }));
    }


    void pause(Output output) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            output.println("Sleep problem");
        }
    }
}
