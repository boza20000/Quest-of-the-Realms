package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
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

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        GameServices services = new GameServices(output);

        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,
                GameConstants.PLAYER_START.x(),
                GameConstants.PLAYER_START.y(),
                "Spawn",
                null,
                null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null,
                null,
                false
        );

        state = new GameState(player, services);
        quest = new NorthExploration(player);
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().get(0));
        for (var m : quest.getMissions()) {
            m.setState(state);
        }
    }

    @Test
    @DisplayName("Travel_North completes when player reaches northern boundary")
    void travelNorthMissionCompletesProperly() {
        var mission = player.getCurMission();

        // Player not yet at northern boundary
        player.move(player.getX(), GameConstants.North_Y + 1);
        assertFalse(mission.checkCompletion(), "Mission should not complete before reaching north");

        // Player reaches northern boundary
        player.move(player.getX(), GameConstants.North_Y - 1);
        assertTrue(mission.checkCompletion(), "Mission should complete at northern boundary");
        assertTrue(mission.isCompleted(), "Mission flag should persist after completion");
    }

    @Test
    @DisplayName("Investigate_Northern_Villages completes only when all villages searched and villagers talked to")
    void investigateVillagesMissionCompletesProperly() {
        var mission = quest.getMissions().get(1);

        assertFalse(mission.checkCompletion(), "Mission should not complete initially");

        // Partially done
        quest.setSearchedVillage1(true);
        quest.setSearchedVillage2(true);
        quest.setTalkedToVillager1(true);
        assertFalse(mission.checkCompletion(), "Mission incomplete until all conditions met");

        // Fully done
        quest.setTalkedToVillager2(true);
        assertTrue(mission.checkCompletion(), "Mission should complete when all conditions met");
        assertTrue(mission.isCompleted(), "Mission flag should persist after completion");
    }

    @Test
    @DisplayName("NorthExploration quest completes only after all missions done")
    void questCompletesOnlyWhenAllMissionsCompleted() {
        QuestFactory q = new QuestFactory(player);

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
