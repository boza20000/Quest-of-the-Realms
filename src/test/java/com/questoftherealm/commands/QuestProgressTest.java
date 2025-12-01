package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.MissionConditionType;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.expeditions.missions.Meet_the_Elder;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class QuestProgressTest {

    private Player player;
    private CompleteQuestCommand questProgressCommand;
    private GameState state;
    private GameServices services;
    private Output output;

    @BeforeEach
    void setup() {
        player = new Player("TestHero", PlayerTypes.Warrior,state);
        output = new ConsoleOutput();
        services = new GameServices(output);
        state = new GameState(player, services);
        questProgressCommand = new CompleteQuestCommand();
    }

    @Test
    void testMissionCompletion() {
        assertInstanceOf(StartQuest.class, player.getCurQuest(), "Should be Start quest instance");
        StartQuest quest = (StartQuest) player.getCurQuest();
        quest.setElderHasTalked(true);
        quest.getMissions().forEach(m->{
            m.setState(state);
            m.setPlayer(player);
        }
        );
        player.updateQuestStatus(state);
    }

    @Test
    void testQuestCompletion() {
        ItemRegistry itemRegistry = new ItemRegistry(new LocalizationService());
        assertInstanceOf(StartQuest.class, player.getCurQuest(), "Should be Start quest instance");
        StartQuest quest = (StartQuest) player.getCurQuest();
        quest.setElderHasTalked(true);
        quest.getMissions().forEach(m->{
                    m.setState(state);
                    m.setPlayer(player);
                }
        );
        player.getInventory().addItem(itemRegistry.getItem("Health Potion"),2,state);
        player.getInventory().addItem(itemRegistry.getItem("Forest Berries"),5,state);
        player.updateQuestStatus(state);
    }


    @Test
    void testNoQuestActive() {
        Output mockOut = mock(Output.class);
        GameServices mockServices = mock(GameServices.class);
        GameState mockState = mock(GameState.class);
        when(mockState.getGameServices()).thenReturn(mockServices);
        when(mockServices.getOutput()).thenReturn(mockOut);

        player.setCurQuest(null);
        player.setCurMission(null);

        questProgressCommand.execute(new String[]{"progress"}, player, mockState);

        verify(mockOut).println(contains("Error: No quest loaded"));
    }

    @Test
    void testNoMissionActive() {
        Output mockOut = mock(Output.class);
        GameServices mockServices = mock(GameServices.class);
        GameState mockState = mock(GameState.class);
        when(mockState.getGameServices()).thenReturn(mockServices);
        when(mockServices.getOutput()).thenReturn(mockOut);

        player.setCurQuest(new StartQuest());
        player.setCurMission(null);

        questProgressCommand.execute(new String[]{"progress"}, player, mockState);
        verify(mockOut).println(contains("Error: No mission loaded"));
    }
}
