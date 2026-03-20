package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.quest.QuestFactory;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("North Exploration Quest — Mission & Quest Logic Tests")
class NorthExplorationTest {

    private Player player;
    private NorthExploration quest;
    private GameState state;
    private Output output;
    private Input input;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        input = mock(Input.class);
        GameServices services = new GameServices(output,input);

        state = new GameState("test-room", services);
        player = new Player("TestHero", PlayerTypes.Warrior, state);
        state.addPlayer(player);
        quest = new NorthExploration(player, state);
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().get(0));
        for (var m : quest.getMissions()) {
            m.setState(state);
        }
    }

    @Test
    @DisplayName("Travel_North completes when player reaches northern boundary")
    void givenPlayerYPositions_whenCheckCompletion_thenTravelNorthCompletesAtBoundary() {
        var mission = player.getCurMission();

        // Player not yet at northern boundary
        player.move(player.getX(), GameConstants.North_Y + 1);
        mission.checkCompletion();
        assertFalse(mission.isCompleted(), "Mission should not complete before reaching north");

        // Player reaches northern boundary
        player.move(player.getX(), GameConstants.North_Y - 1);
        mission.checkCompletion();
        assertTrue(mission.isCompleted(), "Mission flag should persist after completion");
    }

    @Test
    @DisplayName("Investigate_Northern_Villages completes only when all villages searched and villagers talked to")
    void givenVillageProgressFlags_whenCheckCompletion_thenInvestigateMissionCompletesOnlyWhenAllTrue() {
        var mission = quest.getMissions().get(1);

        mission.checkCompletion();
        assertFalse(mission.isCompleted());
        // Partially done
        quest.setSearchedVillage1(true);
        quest.setSearchedVillage2(true);
        quest.setTalkedToVillager1(true);
        mission.checkCompletion();
        assertFalse(mission.isCompleted(), "Mission incomplete until all conditions met");

        // Fully done
        quest.setTalkedToVillager2(true);
        mission.checkCompletion();
        assertTrue(mission.isCompleted(), "Mission should complete when all conditions met");
    }

    @Test
    @DisplayName("NorthExploration quest completes only after all missions done")
    void givenMissionCompletionStates_whenUpdateStatus_thenNorthExplorationCompletionMatchesAllMissionsState() {
        QuestFactory q = new QuestFactory(player, state);

        for (var m : quest.getMissions()) {
            m.setState(state);
        }

        player.setQuestFactory(q);
        for (var m : quest.getMissions()) {
            m.setCompleted(true);
        }
        quest.updateStatus(state);
        assertTrue(quest.isCompleted(), "Quest should complete when all missions done");

        quest.getMissions().get(0).setCompleted(false);
        quest.setCompleted(false);
        quest.updateStatus(state);
        assertFalse(quest.isCompleted(), "Quest should not complete if any mission is incomplete");
    }
}
