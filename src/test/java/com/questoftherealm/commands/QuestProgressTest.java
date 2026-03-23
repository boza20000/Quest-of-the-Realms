package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;

import com.questoftherealm.expeditions.quest.quests.StartQuest;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import static org.mockito.Mockito.*;

class QuestProgressTest {

    private Player player;
    private CompleteQuestCommand questProgressCommand;
    private GameState state;
    private Output output;
    private Input input;


    @BeforeEach
    void setup() {

        output = mock(Output.class);
        input = mock(Input.class);
        GameServices services = new GameServices(output,input);
        state = mock(GameState.class);
        when(state.getGameServices()).thenReturn(services);
        questProgressCommand = new CompleteQuestCommand();
        LocalizationService localizationService = new LocalizationService();
        ItemRegistry itemRegistry = new ItemRegistry(localizationService);
        when(state.getMessages()).thenReturn(localizationService);
        when(state.getItemRegistry()).thenReturn(itemRegistry);
        player = spy(new Player("TestHero", PlayerTypes.Warrior, state));

    }


    @Test
    void givenStartQuestMission_whenQuestStatusUpdated_thenMissionCanProgress() {
        assertInstanceOf(StartQuest.class, player.getCurQuest(), "Should be Start quest instance");
        StartQuest quest = (StartQuest) player.getCurQuest();
        quest.setElderHasTalked(true);
        quest.getMissions().forEach(m -> {
                    m.setState(state);
                    m.setPlayer(player);
                }
        );
        player.updateQuestStatus(state);
    }

    @Test
    void givenRequiredItemsAndFlags_whenQuestStatusUpdated_thenQuestCanProgress() {
        ItemRegistry itemRegistry = new ItemRegistry(new LocalizationService());
        assertInstanceOf(StartQuest.class, player.getCurQuest(), "Should be Start quest instance");
        StartQuest quest = (StartQuest) player.getCurQuest();
        quest.setElderHasTalked(true);
        quest.getMissions().forEach(m -> {
                    m.setState(state);
                    m.setPlayer(player);
                }
        );
        player.getInventory().addItem(itemRegistry.getItem("Health Potion"), 2, state);
        player.getInventory().addItem(itemRegistry.getItem("Forest Berries"), 5, state);
        player.updateQuestStatus(state);
    }


    @Test
    void givenNoActiveQuest_whenExecute_thenPrintsNoQuestError() {

        player.setCurQuest(null);
        player.setCurMission(null);

        questProgressCommand.execute(new String[]{"progress"}, player, state);
        verify(output).println(contains("Error: No quest loaded"));

    }

    @Test
    void givenNoActiveMission_whenExecute_thenPrintsNoMissionError() {

        player.setCurQuest(new StartQuest());
        player.setCurMission(null);

        questProgressCommand.execute(new String[]{"progress"}, player, state);
        verify(output).println(contains("Error: No mission loaded"));

    }
}
