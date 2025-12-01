package com.questoftherealm.FriendlyEntitiesTest;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ElderTest {

    private Elder elder;
    private Player player;
    private GameState state;
    private Output output;
    private MissionInteractions interactions;
    private GameServices services;

    @BeforeEach
    void setup() {


        output = mock(Output.class);
        interactions = mock(MissionInteractions.class);
        services = new GameServices(output);
        state = new GameState(player, services);
        Player realPlayer = new Player("Test", PlayerTypes.Mage, state);
        player = spy(realPlayer);
        elder = new Elder("elder1", state, interactions);
    }

    @Test
    void talkingWithElderFirstTimeGivesRewards() {
        StartQuest quest = mock(StartQuest.class);
        when(player.getCurQuest()).thenReturn(quest);
        when(quest.isElderHasTalked()).thenReturn(false);
        Inventory inv = mock(Inventory.class);
        when(player.getInventory()).thenReturn(inv);
        elder.talk(state, player, true);
        verify(quest).setElderHasTalked(true);
        verifyNoInteractions(interactions);
    }

    @Test
    void talkingWithElderSecondTimePrintsAlreadyTalked() {
        StartQuest quest = mock(StartQuest.class);
        when(player.getCurQuest()).thenReturn(quest);
        when(quest.isElderHasTalked()).thenReturn(true);

        elder.talk(state, player, true);

        // Should print the "already talked" message
        verify(output).println(contains("There is nothing else to be said"));
    }
}
