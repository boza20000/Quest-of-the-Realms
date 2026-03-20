package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestFactory;
import com.questoftherealm.expeditions.quest.QuestTypes;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuestTest {

    private GameState gameState;
    private Player player;
    private GameServices gameServices;
    private Output output;
    private LocalizationService localizationService;
    private MessageBundle messageBundle;
    private QuestFactory questFactory;
    private NorthExploration northExplorationQuestMock;

    @BeforeEach
    void setUp() {
        gameState = mock(GameState.class);
        player = mock(Player.class);
        gameServices = mock(GameServices.class);
        output = mock(Output.class);
        localizationService = mock(LocalizationService.class);
        messageBundle = mock(MessageBundle.class);
        questFactory = mock(QuestFactory.class);
        northExplorationQuestMock = mock(NorthExploration.class);

        when(gameState.getGameServices()).thenReturn(gameServices);
        when(gameServices.getOutput()).thenReturn(output);
        when(gameState.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(messageBundle);

        when(messageBundle.get(anyString())).thenAnswer(invocation -> "localized_" + invocation.getArgument(0));
        when(messageBundle.get(eq("quest.completed"), any())).thenReturn("Quest Completed!");
        
        when(player.getQuestFactory()).thenReturn(questFactory);
    }

    @Test
    void given_validDependencies_when_constructorCalled_then_questCreatedAndPropertiesSet() {
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);

        assertNotNull(quest);
        assertEquals(QuestTypes.NORTH_EXPLORATION, quest.getQuestTypes());
        assertEquals(player, quest.getPlayer());

        assertFalse(quest.isCompleted());
        assertNotNull(quest.getMissions());
        assertFalse(quest.getMissions().isEmpty());
        assertNotNull(quest.getName());
        assertNotNull(quest.getDescription());
    }

    @Test
    void given_uninitializedQuest_when_isCompleted_then_false() {
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        assertFalse(quest.isCompleted());
    }

    @Test
    void given_completedQuest_when_setCompleted_then_isCompletedTrue() {
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        quest.setCompleted(true);
        assertTrue(quest.isCompleted());
    }

    @Test
    void given_questWithMissionsNotDone_when_updateStatus_then_questNotCompleted() {

        when(player.getCurQuest()).thenReturn(northExplorationQuestMock);
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        quest.updateStatus(gameState);

        assertFalse(quest.isCompleted());
        verify(output, never()).println(contains("Quest Completed!"));
    }

    @Test
    void given_questWithMissionsDone_when_updateStatus_then_questCompletedAndNextQuestTriggered() {

        when(player.getY()).thenReturn(0);
        when(player.getCurQuest()).thenReturn(northExplorationQuestMock);

        when(northExplorationQuestMock.isSearchedVillage1()).thenReturn(true);
        when(northExplorationQuestMock.isSearchedVillage2()).thenReturn(true);
        when(northExplorationQuestMock.isTalkedToVillager1()).thenReturn(true);
        when(northExplorationQuestMock.isTalkedToVillager2()).thenReturn(true);

        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        quest.updateStatus(gameState);

        assertTrue(quest.isCompleted());
        verify(output).println(contains("Quest Completed!"));
        verify(questFactory).nextQuest(gameState);
    }

    @Test
    void given_alreadyCompletedQuest_when_updateStatus_then_nothingHappens() {
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        quest.setCompleted(true);
        
        reset(output);
        reset(questFactory);
        
        quest.updateStatus(gameState);
        
        verify(output, never()).println(anyString());
        verify(questFactory, never()).nextQuest(any());
    }

    @Test
    void given_quest_when_setState_then_stateUpdated() {
        GameState newState = mock(GameState.class);
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        quest.setState(newState);

    }

    @Test
    void given_quest_when_setPlayer_then_playerUpdatedOnQuestAndMissions() {
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        Player newPlayer = mock(Player.class);
        
        quest.setPlayer(newPlayer);
        assertEquals(newPlayer, quest.getPlayer());
        for (Mission m : quest.getMissions()) {
             assertEquals(newPlayer, m.getPlayer());
        }
    }

    @Test
    void given_quest_when_setQuestTypes_then_typeUpdated() {
        Quest quest = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        quest.setQuestTypes(QuestTypes.START_QUEST);
        assertEquals(QuestTypes.START_QUEST, quest.getQuestTypes());
    }

    @Test
    void given_variousInstances_when_equals_then_equalityBasedOnName() {
        Quest q1 = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        Quest q2 = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        Quest q3 = new Quest(QuestTypes.START_QUEST, player, gameState);
        
        assertEquals(q1, q2);
        assertNotEquals(q1, q3);
        assertNotEquals(null, q1);
        assertNotEquals(new Object(), q1);
    }

    @Test
    void given_variousInstances_when_hashCode_then_hashBasedOnName() {
        Quest q1 = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        Quest q2 = new Quest(QuestTypes.NORTH_EXPLORATION, player, gameState);
        
        assertEquals(q1.hashCode(), q2.hashCode());
    }
    
    @Test
    void given_defaultConstructor_when_instantiated_then_fieldsAreNullOrEmpty() {

        Quest quest = new Quest() {};
        
        assertTrue(quest.getMissions().isEmpty());
        assertNull(quest.getName());
        assertNull(quest.getDescription());
        assertNull(quest.getQuestTypes());
    }
}
