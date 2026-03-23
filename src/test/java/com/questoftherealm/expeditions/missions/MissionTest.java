package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.quests.StartQuest;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MissionTest {

    private GameState gameState;
    private MessageBundle messageBundle;
    private Player player;
    private GameServices gameServices;
    private Output output;
    private StartQuest startQuest;
    private LocalizationService serviceLoc;

    @BeforeEach
    void setUp() {
        gameState = mock(GameState.class);
        messageBundle = mock(MessageBundle.class);
        player = mock(Player.class);
        gameServices = mock(GameServices.class);
        output = mock(Output.class);
        startQuest = mock(StartQuest.class);
        serviceLoc = mock(LocalizationService.class);

        when(gameState.getMessages()).thenReturn(serviceLoc);
        when(serviceLoc.getBundle()).thenReturn(messageBundle);
        when(gameState.getGameServices()).thenReturn(gameServices);
        when(gameServices.getOutput()).thenReturn(output);

        when(messageBundle.get(anyString())).thenAnswer(invocation -> "localized_" + invocation.getArgument(0));
        when(messageBundle.get(eq("mission.completed"), any())).thenReturn("Mission Completed!");
    }

    @Test
    void given_validDependencies_when_constructorCalled_then_missionCreatedAndLocalized() {
        Mission mission = new Mission(Missions.MEET_ELDER, player, gameState);

        assertNotNull(mission);
        assertEquals(Missions.MEET_ELDER, mission.getMissionType());
        assertEquals(player, mission.getPlayer());
        assertFalse(mission.isCompleted());

        verify(messageBundle, atLeastOnce()).get(contains("name")); 
        verify(messageBundle, atLeastOnce()).get(contains("task"));
    }

    @Test
    void given_jsonProperties_when_jsonConstructorCalled_then_missionCreatedWithProperties() {
        Mission mission = new Mission("Test Mission", "Do something", true, Missions.MEET_ELDER);

        assertEquals("Test Mission", mission.getName());
        assertEquals("Do something", mission.getTask());
        assertTrue(mission.isCompleted());
        assertEquals(Missions.MEET_ELDER, mission.getMissionType());
        assertNull(mission.getPlayer());
    }

    @Test
    void given_conditionMet_when_checkCompletion_then_missionIsCompletedAndMessagePrinted() {
        when(player.getCurQuest()).thenReturn(startQuest);
        when(startQuest.isElderHasTalked()).thenReturn(true);

        Mission mission = new Mission(Missions.MEET_ELDER, player, gameState);
        mission.checkCompletion();

        assertTrue(mission.isCompleted());
        verify(output).println(contains("Mission Completed!"));
    }

    @Test
    void given_conditionNotMet_when_checkCompletion_then_missionIsNotCompletedAndNoMessagePrinted() {
        // Setup MEET_ELDER condition: False
        when(player.getCurQuest()).thenReturn(startQuest);
        when(startQuest.isElderHasTalked()).thenReturn(false);

        Mission mission = new Mission(Missions.MEET_ELDER, player, gameState);
        mission.checkCompletion();

        assertFalse(mission.isCompleted());
        verify(output, never()).println(anyString());
    }

    @Test
    void given_alreadyCompleted_when_checkCompletion_then_noMessagePrinted() {
        Mission mission = new Mission(Missions.MEET_ELDER, player, gameState);
        mission.setCompleted(true);

        reset(output);
        mission.checkCompletion();
        verify(output, never()).println(anyString());
    }

    @Test
    void given_variousInstances_when_equals_then_equalityBasedOnName() {
        Mission m1 = new Mission("Name", "Task", false, Missions.MEET_ELDER);
        Mission m2 = new Mission("Name", "OtherTask", true, Missions.GATHER_SUPPLIES);
        Mission m3 = new Mission("OtherName", "Task", false, Missions.MEET_ELDER);

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(null, m1);
        assertNotEquals(new Object(), m1);
    }

    @Test
    void given_missionType_when_setMissionType_then_typeIsUpdated() {
        Mission mission = new Mission();
        mission.setMissionType(Missions.MEET_ELDER);
        assertEquals(Missions.MEET_ELDER, mission.getMissionType());
    }

    @Test
    void given_gameState_when_setState_then_stateIsUpdated() {
        Mission mission = new Mission("Test Mission", "Task", false, Missions.MEET_ELDER);
        mission.setState(gameState);

        mission.complete();
        assertTrue(mission.isCompleted());
        verify(output).println(contains("Mission Completed!"));
    }

    @Test
    void given_player_when_setPlayer_then_playerIsUpdated() {
        Mission mission = new Mission("Name", "Task", false, Missions.MEET_ELDER);
        mission.setPlayer(player);
        assertEquals(player, mission.getPlayer());
    }

    @Test
    void given_completedStatus_when_setCompleted_then_statusIsUpdated() {
        Mission mission = new Mission();
        mission.setCompleted(true);
        assertTrue(mission.isCompleted());
        mission.setCompleted(false);
        assertFalse(mission.isCompleted());
    }

    @Test
    void given_sameName_when_hashCode_then_hashCodesAreEqual() {
        Mission m1 = new Mission("SameName", "Task1", false, Missions.MEET_ELDER);
        Mission m2 = new Mission("SameName", "Task2", true, Missions.GATHER_SUPPLIES);
        
        assertEquals(m1.hashCode(), m2.hashCode());
    }

}
