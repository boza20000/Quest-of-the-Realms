package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("Rise of the Goblin Threat Quest — Mission & Quest Logic Tests")
class RiseOfTheGoblinThreatTest {

    private Player player;
    private RiseOfTheGoblinThreat quest;
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
                null, null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null,
                false
        );

        state = new GameState(player, services);

        quest = new RiseOfTheGoblinThreat(player);
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().get(0));

        for (var m : quest.getMissions()) {
            m.setState(state);
        }
    }

    @Test
    @DisplayName("Warn_the_Castle completes only when player reports to king at castle")
    void warnCastleMissionCompletesProperly() {
        var mission = player.getCurMission();

        // Not reported yet
        quest.setReportedToKing(false);
        assertFalse(mission.checkCompletion());

        // Reported at wrong position
        player.move(GameConstants.PLAYER_START.x(), GameConstants.PLAYER_START.y()-1);
        quest.setReportedToKing(true);
        assertFalse(mission.checkCompletion(),"two conditions fulfilled king talked and position is not castle and quest is RiseOfTheGoblinThreat");

        // Correct position & reported
        player.move(GameConstants.Castle.x(), GameConstants.Castle.y());
        quest.setReportedToKing(true);
        assertTrue(mission.checkCompletion());
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Assemble_an_Army completes only after trying all recruitments and reporting to king")
    void assembleArmyMissionCompletesProperly() {
        var mission = quest.getMissions().get(1);

        // Not yet tried recruitments
        quest.setKnightsTriedToRecruit(false);
        quest.setArchersTriedToRecruit(true);
        quest.setMagesTriedToRecruit(true);
        quest.setReportedToKing(true);
        assertFalse(mission.checkCompletion());

        // All recruitments tried
        quest.setKnightsTriedToRecruit(true);
        quest.setArchersTriedToRecruit(true);
        quest.setMagesTriedToRecruit(true);
        quest.setReportedToKing(true);

        // Army power before recruitment
        int beforePower = quest.getArmyPower();
        // Some recruited
        quest.setKnightsRecruited(true);
        quest.setArchersRecruited(true);
        quest.setMagesRecruited(false);

        assertTrue(mission.checkCompletion());
        assertTrue(mission.isCompleted());
        assertEquals(beforePower + 15 + 20, quest.getArmyPower()); // Knights + Archers
    }

    @Test
    @DisplayName("Defeat_the_Goblin_General completes only after isDefeated true")
    void defeatGoblinGeneralMissionCompletesProperly() {
        var mission = quest.getMissions().get(2);

        quest.setDefeated(false);
        assertFalse(mission.checkCompletion());

        quest.setDefeated(true);
        assertTrue(mission.checkCompletion());
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("RiseOfTheGoblinThreat quest completes only after all missions done")
    void questCompletesOnlyAfterAllMissionsDone() {
        QuestFactory q = new QuestFactory(player);
        player.setQuestFactory(q);

        for (var m : quest.getMissions()) {
            m.setCompleted(true);
        }

        quest.updateStatus(state);
        assertTrue(quest.isCompleted(), "Quest should complete when all missions done");

        // Make one mission incomplete
        quest.getMissions().get(0).setCompleted(false);
        quest.setCompleted(false);
        quest.updateStatus(state);
        assertFalse(quest.isCompleted(), "Quest should not complete if any mission is incomplete");
    }
}
