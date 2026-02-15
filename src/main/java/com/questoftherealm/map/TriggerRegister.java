package com.questoftherealm.map;

import com.questoftherealm.expeditions.quest.quests.FinalBattle;
import com.questoftherealm.expeditions.quest.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
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

    public TriggerRegister(GameState state) {
        this.state = state;
        this.recruitmentManager = new RecruitmentManager(state);
        this.missionInteractions = new MissionInteractions(state);
        this.goblinGeneralManager = new GoblinGeneralManager(state);
        this.goblinKingManager = new GoblinKingManager(state);
        registerTriggers();
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
            if (TriggerConditions.NORTH_VILLAGE.matches(player)) {
                missionInteractions.villageIntro_1();
                ((NorthExploration) player.getCurQuest()).setSearchedVillage1(true);
                return true;
            }
            return false;
        }));

        // Village 2
        triggers.add(new LocationTrigger(GameConstants.NorthVillage_2, player -> {
            if (TriggerConditions.NORTH_VILLAGE.matches(player)) {
                missionInteractions.villageIntro_2();
                ((NorthExploration) player.getCurQuest()).setSearchedVillage2(true);
                return true;
            }
            return false;
        }));
    }

    private void registerGoblinCampTriggers() {
        // Goblin Camp Search
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            if (TriggerConditions.GOBLIN_CAMP_SEARCH.matches(player)) {
                missionInteractions.goblinCampSpotted();
                ((GoblinAmbush) player.getCurQuest()).setCampFound(true);
                player.updateQuestStatus(state);
                return true;
            }
            return false;
        }));

        // Goblin Camp Listening
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            if (TriggerConditions.GOBLIN_CAMP_INFILTRATION.matches(player)) {
                missionInteractions.goblinsTalkingOverheard();
                ((GoblinAmbush) player.getCurQuest()).setPlayerAmbushed(true);
                player.updateQuestStatus(state);
                return true;
            }
            return false;
        }));

        // Goblin Camp Escape
        triggers.add(new LocationTrigger(GameConstants.Goblin_Camp, player -> {
            if (TriggerConditions.GOBLIN_CAMP_ESCAPE.matches(player)) {
                missionInteractions.makeDecision(player);
                player.updateQuestStatus(state);
                return true;
            }
            return false;
        }));
    }

    private void registerAssemblyArmyTriggers() {
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_1, player -> {
            if (TriggerConditions.ARMY_ASSEMBLING_KNIGHTS.matches(player) && !((RiseOfTheGoblinThreat)player.getCurQuest()).isKnightsRecruited()) {
                recruitmentManager.talkToTheKnights(player, state);
                return true;
            }
            return false;
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.SouthVillage_2, player -> {
            if (TriggerConditions.ARMY_ASSEMBLING_ARCHERS.matches(player) && !((RiseOfTheGoblinThreat)player.getCurQuest()).isArchersRecruited()) {
                recruitmentManager.talkToTheArchers(player, state);
                return true;
            }
            return false;
        }));
        // Army assembling
        triggers.add(new LocationTrigger(GameConstants.MagesOutPost, player -> {
            if (TriggerConditions.ARMY_ASSEMBLING_MAGES.matches(player) && !((RiseOfTheGoblinThreat)player.getCurQuest()).isMagesRecruited()) {
                recruitmentManager.talkToTheMages(player, state);
                return true;
            }
            return false;
        }));
    }

    private void registerArmyFightTriggers() {
        // Army fight
        triggers.add(new LocationTrigger(GameConstants.Battlefield, player -> {
            if (TriggerConditions.ARMY_FIGHT.matches(player)) {
                goblinGeneralManager.startFinalBattle(player, ((RiseOfTheGoblinThreat)player.getCurQuest()), state);
                return true;
            }
            return false;
        }));
        //Goblin Cave
        triggers.add(new LocationTrigger(GameConstants.FarNorthMountain, player -> {
            if (TriggerConditions.GOBLIN_CAVE.matches(player)) {
                goblinKingManager.goblinKingdomFound(player, ((FinalBattle)player.getCurQuest()));
                return true;
            }
            return false;
        }));
    }

}
