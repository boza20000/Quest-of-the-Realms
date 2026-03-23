package com.questoftherealm.FriendlyEntitiesTest;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class KingTest {

    private King king;
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
        king = new King("king1", state, interactions);
    }

    @Test
    void talkingWithKingFirstTimeReportsQuest() {
        RiseOfTheGoblinThreat quest = mock(RiseOfTheGoblinThreat.class);
        when(player.getCurQuest()).thenReturn(quest);
        when(quest.isReportedToKing()).thenReturn(false);

        king.talk(state, player, true);
        verify(quest).setReportedToKing(true);
        verifyNoInteractions(output);
    }

    @Test
    void talkingWithKingAfterReport() {
        RiseOfTheGoblinThreat quest = mock(RiseOfTheGoblinThreat.class);
        when(player.getCurQuest()).thenReturn(quest);
        when(quest.isReportedToKing()).thenReturn(true);

        king.talk(state, player, true);
        verify(output).println(contains("The king urges you to go on you way!"));
    }
}
