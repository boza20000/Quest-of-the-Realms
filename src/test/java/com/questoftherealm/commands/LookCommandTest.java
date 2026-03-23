package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.map.TriggerRegister;
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
        lookCommand = new LookCommand();
        output = mock(Output.class);
        Input input = mock(Input.class);
        GameServices gameServices = new GameServices(output, input);
        state = new GameState("test", gameServices);
        player = spy(new Player("TestHero", PlayerTypes.Warrior, state));
        state.addPlayer(player);
        state.setSimulation(true);

    }

    @Test
    void givenValidLookCommand_whenExecute_thenPrintsLookingAndDelegatesToPlayer() {
        lookCommand.execute(new String[]{"look"}, player, state);
        verify(output).print("Looking");
        verify(player).look(state);
    }

    @Test
    void givenExtraArguments_whenExecute_thenPrintsUsage() {
        lookCommand.execute(new String[]{"look", "extra"}, player, state);
        verify(output).println("Usage: " + lookCommand.getDescription(state));
    }

    @Test
    void givenNullPlayer_whenExecute_thenPrintsNoPlayerError() {
        lookCommand.execute(new String[]{"look"}, null, state);
        verify(output).println("Error: No player loaded.");
    }
}
