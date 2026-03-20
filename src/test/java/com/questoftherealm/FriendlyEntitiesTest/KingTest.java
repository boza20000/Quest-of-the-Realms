package com.questoftherealm.FriendlyEntitiesTest;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.quest.quests.NorthExploration;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

public class KingTest {

    private King king;
    private Player player;
    private GameState state;
    private Output output;
    private Input input;
    private MissionInteractions interactions;
    private GameServices services;
    private RandomService randomService;
    private LocalizationService localizationService;
    private MessageBundle messageBundle;
    private Random random;

    @BeforeEach
    void setup() {
        // Manual mocking
        state = mock(GameState.class);
        player = mock(Player.class);
        output = mock(Output.class);
        input = mock(Input.class);
        interactions = mock(MissionInteractions.class);
        services = mock(GameServices.class);
        randomService = mock(RandomService.class);
        localizationService = mock(LocalizationService.class);
        messageBundle = mock(MessageBundle.class);
        random = mock(Random.class);

        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(services.getRandom()).thenReturn(randomService);
        when(randomService.random()).thenReturn(random);

        when(state.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(messageBundle);
        when(messageBundle.get(anyString())).thenAnswer(i -> "localized_" + i.getArgument(0));

        king = new King("king1", state, interactions);
    }

    @Test
    void given_RiseOfGoblinThreatNotReported_Simulation_when_talk_then_ReferenceUpdatedNoOutput() {
        RiseOfTheGoblinThreat quest = mock(RiseOfTheGoblinThreat.class);
        when(player.getCurQuest()).thenReturn(quest);
        when(quest.isReportedToKing()).thenReturn(false);

        king.talk(state, player, true);

        verify(quest).setReportedToKing(true);
        verifyNoInteractions(output);
    }

    @Test
    void given_RiseOfGoblinThreatNotReported_Real_when_talk_then_ReferenceUpdatedAndDialogPrinted() {
        RiseOfTheGoblinThreat quest = mock(RiseOfTheGoblinThreat.class);
        when(player.getCurQuest()).thenReturn(quest);
        when(quest.isReportedToKing()).thenReturn(false);

        king.talk(state, player, false);

        verify(quest).setReportedToKing(true);
        verify(output, atLeastOnce()).print(anyString()); 
    }

    @Test
    void given_RiseOfGoblinThreatAlreadyReported_when_talk_then_PrintTalked() {
        RiseOfTheGoblinThreat quest = mock(RiseOfTheGoblinThreat.class);
        when(player.getCurQuest()).thenReturn(quest);
        when(quest.isReportedToKing()).thenReturn(true);

        king.talk(state, player, false);

        verify(output).println(contains("localized_king.talked"));
        verify(quest, never()).setReportedToKing(anyBoolean());
    }

    @Test
    void given_DifferentQuest_when_talk_then_PrintTalked() {
        when(player.getCurQuest()).thenReturn(mock(NorthExploration.class));

        king.talk(state, player, false);

        verify(output).println(contains("localized_king.talked"));
    }
}
