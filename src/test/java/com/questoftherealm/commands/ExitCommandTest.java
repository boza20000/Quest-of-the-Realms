package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExitCommandTest {

    private ExitCommand command;
    private Player player;
    private GameState state;
    private Output output;
    private Input input;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        input = mock(Input.class);
        GameServices services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);
        when(services.getInput()).thenReturn(input);

        state = new GameState("test-room", services);
        player = new Player("TestHero", PlayerTypes.Warrior, state);
        state.addPlayer(player);

        command = new ExitCommand();
    }

    @Test
    void givenPrivateModeAndNoSaveChoice_whenExecute_thenMarksGameOverAndDeactivatesPlayer() {
        state.setPrivate(true);
        when(input.nextLine()).thenReturn("N");

        command.execute(new String[]{"exit"}, player, state);

        assertTrue(state.isGameOver());
        assertFalse(player.isActive());
        verify(output, atLeastOnce()).println(anyString());
    }

    @Test
    void givenInvalidArguments_whenExecute_thenPrintsUsageAndKeepsPlayerActive() {
        command.execute(new String[]{"exit", "now"}, player, state);

        assertTrue(player.isActive());
        verify(output).println(contains("Usage"));
    }
}

