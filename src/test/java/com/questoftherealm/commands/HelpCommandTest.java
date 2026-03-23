package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HelpCommandTest {

    private HelpCommand command;
    private CommandFactory factory;
    private GameState state;
    private GameServices services;
    private Player player;
    private Output output;
    private LocalizationService localizationService;
    private MessageBundle bundle;

    @BeforeEach
    void setUp() {
        factory = mock(CommandFactory.class);
        state = mock(GameState.class);
        services = mock(GameServices.class);
        player = mock(Player.class);
        output = mock(Output.class);
        localizationService = mock(LocalizationService.class);
        bundle = mock(MessageBundle.class);
        command = new HelpCommand(factory);

        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(state.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(bundle);
        when(bundle.get(anyString())).thenAnswer(inv -> inv.getArgument(0));
        when(bundle.get(anyString(), any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void givenExtraArguments_whenExecute_thenShowsUsage() {
        String[] args = {"help", "unexpectedArg"};

        command.execute(args, player, state);
        verify(output).println(contains("help.usage"));
        verify(factory, never()).getAllCommands();
    }

    @Test
    void givenNoExtraArguments_whenExecute_thenShowsAllCommands() {
        String[] args = {"help"};
        Map<String, Command> commandsMap = new HashMap<>();
        Command cmd1 = mock(Command.class);

        when(cmd1.getDescription(state)).thenReturn("desc1");
        commandsMap.put("cmd1", cmd1);
        when(factory.getAllCommands()).thenReturn(commandsMap);
        command.execute(args, player, state);

        verify(output).println(contains("help.header"));
        verify(factory).getAllCommands();
    }
}

