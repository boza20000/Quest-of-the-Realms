package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class StatsCommandTest {

    private Player player;
    private GameState state;
    private GameServices services;
    private Output output;

    @BeforeEach
    void setup() {

        state = mock(GameState.class);
        services = mock(GameServices.class);
        output = mock(Output.class);
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        LocalizationService localizationService = new LocalizationService();
        ItemRegistry itemRegistry =new ItemRegistry(localizationService);
        when(state.getMessages()).thenReturn(localizationService);
        when(state.getItemRegistry()).thenReturn(itemRegistry);
        player = new Player("TestHero", PlayerTypes.Warrior,state);
        state.setPlayer(player);
    }

    @Test
    void testStatsCommandValid() {
        StatsCommand cmd = new StatsCommand();
        cmd.execute(new String[]{"stats"}, player, state);
        verify(output).println(contains("Player current stats"));
    }

    @Test
    void testStatsCommandInvalidArgs() {
        StatsCommand cmd = new StatsCommand();
        cmd.execute(new String[]{"stats", "extra"}, player, state);
        verify(output).println(contains("Usage"));
    }
}
