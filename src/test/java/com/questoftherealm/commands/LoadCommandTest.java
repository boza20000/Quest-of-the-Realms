package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.LoadGame;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoadCommandTest {

    private LoadCommand command;
    private GameState state;
    private GameServices services;
    private Player player;
    private Output output;
    private LocalizationService localizationService;
    private MessageBundle bundle;
    private LoadGame loadGameMock;
    private MissionInteractions missionInteractionsMock;

    @BeforeEach
    void setUp() {
        state = mock(GameState.class);
        services = mock(GameServices.class);
        player = mock(Player.class);
        output = mock(Output.class);
        localizationService = mock(LocalizationService.class);
        bundle = mock(MessageBundle.class);
        loadGameMock = mock(LoadGame.class);
        missionInteractionsMock = mock(MissionInteractions.class);

        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(state.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(bundle);

        when(bundle.get(anyString())).thenAnswer(inv -> inv.getArgument(0));
        when(bundle.get(anyString(), any())).thenAnswer(inv -> inv.getArgument(0));

        command = new LoadCommand() {
            @Override
            protected LoadGame createLoadGame() {
                return loadGameMock;
            }

            @Override
            protected MissionInteractions createMissionInteractions(GameState state) {
                return missionInteractionsMock;
            }
        };
    }

    @Test
    void givenMultiplayerGame_whenExecute_thenFails() {
        String[] args = {"load", "savefile"};
        when(state.isPrivate()).thenReturn(false);

        command.execute(args, player, state);
        verify(output).println(contains("load.error.notPrivate"));
        verify(loadGameMock, never()).loadGameSave(anyString(), any());
    }

    @Test
    void givenSingleplayerGameAndMissingArgs_whenExecute_thenShowsUsage() {

        String[] args = {"load"};
        when(state.isPrivate()).thenReturn(true);

        command.execute(args, player, state);

        verify(output).println(contains("load.usage"));
        verify(loadGameMock, never()).loadGameSave(anyString(), any());
    }

    @Test
    void givenSingleplayerGameAndValidArgs_whenExecute_thenLoadsGame() {
        String[] args = {"load", "mySave"};
        when(state.isPrivate()).thenReturn(true);
        when(player.getName()).thenReturn("TestPlayer");

        command.execute(args, player, state);

        verify(loadGameMock).loadGameSave("mySave", state);
        verify(state).removePlayer("TestPlayer");
        verify(missionInteractionsMock).worldStart(any());
    }
}

