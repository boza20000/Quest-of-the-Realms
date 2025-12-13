package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LookCommandTest {

    private Player player;
    private LookCommand lookCommand;
    private GameState state;
    private Output output;

    @BeforeEach
    void setup() {

        state = mock(GameState.class);
        output = mock(Output.class);
        GameServices services = mock(GameServices.class);
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        lookCommand = new LookCommand();
        state.setSimulation(true);
        LocalizationService localizationService = new LocalizationService();
        ItemRegistry itemRegistry =new ItemRegistry(localizationService);
        when(state.getMessages()).thenReturn(localizationService);
        when(state.getItemRegistry()).thenReturn(itemRegistry);

        player = spy(new Player("TestHero", PlayerTypes.Warrior,state));

    }

    @Test
    void execute_PrintsLookingAndCallsPlayerLook() throws InterruptedException {
        lookCommand.execute(new String[]{"look"}, player, state);
        verify(output).print("Looking");
        verify(player).look(state);
    }

    @Test
    void execute_PrintsUsage_WhenExtraArgumentsProvided() {
        lookCommand.execute(new String[]{"look", "extra"}, player, state);
        verify(output).println("Usage: " + lookCommand.getDescription(state));
    }

    @Test
    void execute_PrintsError_WhenPlayerIsNull() {
        lookCommand.execute(new String[]{"look"}, null, state);
        verify(output).println("Error: No player loaded.");
    }
}
