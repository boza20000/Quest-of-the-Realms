package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.missions.Mission;
import com.questoftherealm.expeditions.quest.Quest;
import com.questoftherealm.expeditions.quest.QuestFactory;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.contains;

class QuestCommandTest {

    private Player player;
    private GameState state;
    private GameServices services;
    private Output output;
    private QuestFactory questFactory;

    @BeforeEach
    void setup() {
        services = mock(GameServices.class);
        output = mock(Output.class);

        GameState s = new GameState("test-room",services);
        state = spy(s);
        player = spy(new Player("TestHero", PlayerTypes.Warrior, state));
        state.addPlayer(player);
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);

        questFactory = mock(QuestFactory.class);
        doReturn(questFactory).when(player).getQuestFactory();
    }

    @Test
    void givenActiveQuestAndMission_whenExecute_thenPrintsMissionTasks() {
        Quest quest = mock(Quest.class);
        Mission mission = mock(Mission.class);

        when(quest.getMissions()).thenReturn(List.of(mission));
        when(mission.getTask()).thenReturn("Test mission");
        when(questFactory.getCurrentQuest()).thenReturn(quest);

        doReturn(mission).when(player).getCurMission();
        doReturn(quest).when(player).getCurQuest();

        QuestCommand cmd = new QuestCommand();
        cmd.execute(new String[]{"quest"}, player, state);


        verify(output).println("Test mission");
    }

    @Test
    void givenNoQuest_whenExecute_thenPrintsNoQuestError() {
        when(questFactory.getCurrentQuest()).thenReturn(null);

        QuestCommand cmd = new QuestCommand();
        cmd.execute(new String[]{"quest"}, player, state);

        verify(output).println(contains("Error: No quest loaded"));
    }

    @Test
    void givenNoMission_whenExecute_thenPrintsNoMissionError() {
        Quest quest = mock(Quest.class);
        when(quest.getMissions()).thenReturn(java.util.List.of());
        when(questFactory.getCurrentQuest()).thenReturn(quest);

        doReturn(null).when(player).getCurMission();
        doReturn(quest).when(player).getCurQuest();

        QuestCommand cmd = new QuestCommand();
        cmd.execute(new String[]{"quest"}, player, state);

        verify(output).println(contains("Error: No mission loaded"));
    }

    @Test
    void givenExtraArguments_whenExecute_thenPrintsUsage() {
        QuestCommand cmd = new QuestCommand();
        cmd.execute(new String[]{"quest", "extra"}, player, state);
        verify(output).println(contains("Usage"));
    }
}
