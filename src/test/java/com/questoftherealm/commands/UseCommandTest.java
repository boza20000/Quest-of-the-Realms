package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UseCommandTest {

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
        ItemRegistry registry = new ItemRegistry(localizationService);
        when(state.getItemRegistry()).thenReturn(registry);
        when(state.getMessages()).thenReturn(localizationService);
        player = new Player("TestHero", PlayerTypes.Warrior, state);
    }

    @Test
    void givenValidItemInInventory_whenExecute_thenConsumesAndAppliesItem() throws ItemNotFound {
        UseCommand cmd = new UseCommand();
        Item potion = state.getItemRegistry().getItem("Health Potion");
        player.getInventory().addItem(potion, 1,state);
        player.getPlayerCharacter().takeDamage(10,state,player);

        cmd.execute(new String[]{"use", "Health", "Potion"}, player, state);

        assertEquals(player.getPlayerCharacter().getMaxHealth(), player.getPlayerCharacter().getHealth());
        assertFalse(player.getInventory().containsItem(potion));
    }

    @Test
    void givenUnknownItem_whenExecute_thenPrintsItemNotFound() {
        UseCommand cmd = new UseCommand();
        cmd.execute(new String[]{"use", "Nonexistent"}, player, state);
        verify(output).println(contains("Item not found"));
    }

    @Test
    void givenMissingArguments_whenExecute_thenPrintsUsage() {
        UseCommand cmd = new UseCommand();
        cmd.execute(new String[]{"use"}, player, state);
        verify(output).println(contains("Usage"));
    }
}
