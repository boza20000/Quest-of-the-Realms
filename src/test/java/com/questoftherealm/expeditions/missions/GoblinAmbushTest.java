package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.quest.QuestFactory;
import com.questoftherealm.expeditions.quest.quests.GoblinAmbush;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("Goblin Ambush Quest — Mission & Quest Logic Tests")
class GoblinAmbushTest {

    private Player player;
    private GoblinAmbush quest;
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

        quest = new GoblinAmbush(player,state);
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().get(0));

        for (var m : quest.getMissions()) {
            m.setState(state);
            m.setPlayer(player);
        }
    }

    @Test
    @DisplayName("Explore_Nearby_Forests completes only at Goblin Camp")
    void exploreNearbyForestsMissionCompletesProperly() {
        var mission = player.getCurMission();

        // Wrong positions
        player.move(GameConstants.Goblin_Camp.x() + 1, GameConstants.Goblin_Camp.y());
        mission.checkCompletion();
        assertFalse(mission.isCompleted());
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y() + 1);
        mission.checkCompletion();
        assertFalse(mission.isCompleted());

        // Correct position
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y());
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Infiltrate_the_Camp completes only after camp found")
    void infiltrateCampMissionCompletesProperly() {
        var mission = quest.getMissions().get(1);

        // Camp not found yet
        quest.setCampFound(false);
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y());
        mission.checkCompletion();
        assertFalse(mission.isCompleted());

        // Camp found
        quest.setCampFound(true);
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Ambushed completes only when playerAmbushed true and prior missions done")
    void ambushedMissionCompletesProperly() {
        var mission = quest.getMissions().get(2);
        mission.setCompleted(false);

        quest.setPlayerAmbushed(true);
        quest.updateStatus(state);
        mission.checkCompletion();
        assertFalse(mission.isCompleted(),"position should be the camp");
        player.move(GameConstants.Goblin_Camp.x(),GameConstants.Goblin_Camp.y());
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Escape_to_Safety completes only after ambush and playerEscapedAmbush true")
    void escapeToSafetyMissionCompletesProperly() {
        var mission = quest.getMissions().get(3);

        quest.setPlayerEscapedAmbush(false);
        mission.checkCompletion();
        assertFalse(mission.isCompleted());

        quest.setPlayerEscapedAmbush(true);
        mission.checkCompletion();

        assertTrue(mission.isCompleted(),"mission completed camp escaped");
    }

    @Test
    @DisplayName("GoblinAmbush quest completes only after all missions done")
    void questCompletesOnlyAfterAllMissionsDone() {
        QuestFactory q = new QuestFactory(player, state);
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
