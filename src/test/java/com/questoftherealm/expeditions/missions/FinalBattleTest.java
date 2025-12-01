package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("Final Battle Quest — Mission & Quest Logic Tests")
class FinalBattleTest {

    private Player player;
    private FinalBattle quest;
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

        quest = new FinalBattle(player);
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().get(0));

        for (var m : quest.getMissions()) {
            m.setState(state);
        }
    }

    @Test
    @DisplayName("March_Into_the_Far_North completes only when player reaches Far North")
    void marchIntoFarNorthMissionCompletesProperly() {
        var mission = player.getCurMission();

        // Not there yet
        player.move(GameConstants.PLAYER_START.x(), GameConstants.PLAYER_START.y());
        assertFalse(mission.checkCompletion());

        // Move to Far North
        player.move(GameConstants.FarNorthMountain.x(), GameConstants.FarNorthMountain.y());
        assertTrue(mission.checkCompletion());
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Breach_the_Stronghold completes only after isBreached true")
    void breachStrongholdMissionCompletesProperly() {
        var mission = quest.getMissions().get(1);

        quest.setBreached(false);
        assertFalse(mission.checkCompletion());

        quest.setBreached(true);
        assertTrue(mission.checkCompletion());
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Defeat_the_Goblin_King completes only after isDefeated true")
    void defeatGoblinKingMissionCompletesProperly() {
        var mission = quest.getMissions().get(2);

        quest.setDefeated(false);
        assertFalse(mission.checkCompletion());

        quest.setDefeated(true);
        assertTrue(mission.checkCompletion());
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("FinalBattle quest completes only after all missions done")
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
