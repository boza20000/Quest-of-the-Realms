package com.questoftherealm.commands;

import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandFactoryTest {

    private CommandFactory commandFactory;
    private GameState state;

    @BeforeEach
    void setup() {
        Output output = mock(Output.class);
        GameServices services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);
        state = new GameState("test-room", services);
        commandFactory = new CommandFactory();
    }

    @Test
    void givenRegisteredCommandName_whenGetCommand_thenReturnsConcreteCommand() {
        Command command = commandFactory.getCommand("attack", state);
        assertNotNull(command);
        assertInstanceOf(AttackCommand.class, command);
    }

    @Test
    void givenUnknownCommandName_whenGetCommand_thenThrowsInvalidCommand() {
        assertThrows(InvalidCommand.class, () -> commandFactory.getCommand("nonexistent", state));
    }
}

