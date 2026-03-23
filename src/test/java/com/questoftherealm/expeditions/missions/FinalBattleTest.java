package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.quest.QuestFactory;
import com.questoftherealm.expeditions.quest.quests.FinalBattle;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("Final Battle Quest — Mission & Quest Logic Tests")
class FinalBattleTest {

    private Player player;
    private FinalBattle quest;
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

        quest = new FinalBattle(player, state);
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().get(0));

        for (var m : quest.getMissions()) {
            m.setState(state);
        }
    }

    @Test
    @DisplayName("March_Into_the_Far_North completes only when player reaches Far North")
    void givenPlayerPositions_whenCheckCompletion_thenMarchIntoFarNorthCompletesOnlyAtTarget() {
        var mission = player.getCurMission();

        // Not there yet
        player.move(GameConstants.PLAYER_START.x(), GameConstants.PLAYER_START.y());
        mission.checkCompletion();
        assertFalse(mission.isCompleted());

        // Move to Far North
        player.move(GameConstants.FarNorthMountain.x(), GameConstants.FarNorthMountain.y());
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Breach_the_Stronghold completes only after isBreached true")
    void givenBreachedFlag_whenCheckCompletion_thenBreachStrongholdCompletesOnlyWhenTrue() {
        var mission = quest.getMissions().get(1);

        quest.setBreached(false);
        mission.checkCompletion();
        assertFalse(mission.isCompleted());

        quest.setBreached(true);
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Defeat_the_Goblin_King completes only after isDefeated true")
    void givenDefeatedFlag_whenCheckCompletion_thenDefeatGoblinKingCompletesOnlyWhenTrue() {
        var mission = quest.getMissions().get(2);

        quest.setDefeated(false);
        mission.checkCompletion();
        assertFalse(mission.isCompleted());

        quest.setDefeated(true);
        mission.checkCompletion();
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("FinalBattle quest completes only after all missions done")
    void givenMissionCompletionStates_whenUpdateStatus_thenFinalBattleCompletionMatchesAllMissionsState() {
        QuestFactory q = new QuestFactory(player,state);
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
