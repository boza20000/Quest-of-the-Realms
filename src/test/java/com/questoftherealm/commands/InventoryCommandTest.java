package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class InventoryCommandTest {

    private InventoryCommand command;
    private GameState state;
    private GameServices services;
    private Player player;
    private Output output;
    private LocalizationService localizationService;
    private MessageBundle bundle;

    @BeforeEach
    void setUp() {
        command = new InventoryCommand();
        state = mock(GameState.class);
        services = mock(GameServices.class);
        player = mock(Player.class);
        output = mock(Output.class);
        localizationService = mock(LocalizationService.class);
        bundle = mock(MessageBundle.class);

        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(state.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(bundle);

        when(bundle.get(anyString())).thenAnswer(inv -> inv.getArgument(0));
        when(bundle.get(anyString(), any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void givenExtraArguments_whenExecute_thenShowsUsage() {
        String[] args = {"inventory", "unexpectedArg"};

        command.execute(args, player, state);
        verify(output).println(contains("inventory.usage"));
        verify(player, never()).openInventory(any());
    }

    @Test
    void givenSingleArgument_whenExecute_thenOpensInventory() {
        String[] args = {"inventory"};

        command.execute(args, player, state);
        verify(player).openInventory(state);
    }
}

