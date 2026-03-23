package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.map.WorldMap;
import com.questoftherealm.map.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TakeCommandTest {

    private Player player;
    private GameState state;
    private GameServices services;
    private Output output;
    private WorldMap map;
    private Tile tile;
    private ItemRegistry registry;

    @BeforeEach
    void setup() {
        state = mock(GameState.class);
        services = mock(GameServices.class);
        output = mock(Output.class);
        LocalizationService localizationService = new LocalizationService();
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(state.getMessages()).thenReturn(localizationService);
        map = mock(WorldMap.class);
        tile = mock(Tile.class);
        when(state.getMap()).thenReturn(map);
        when(map.curZone(anyInt(), anyInt())).thenReturn(tile);
        registry = new ItemRegistry(localizationService);
        when(state.getItemRegistry()).thenReturn(registry);
        player = new Player("TestHero", PlayerTypes.Warrior, state);

    }

    @Test
    void givenItemDropWithEnoughQuantity_whenExecute_thenAddsToInventoryAndRemovesDrop() {
        TakeCommand cmd = new TakeCommand();
        Item sword = registry.getItem("Bronze Sword");
        when(tile.takeItem(sword, 2)).thenReturn(true);

        cmd.execute(new String[]{"take", "Bronze", "Sword", "2"}, player, state);

        assertEquals(2, player.getInventory().getQuantity(sword));
        verify(tile).takeItem(sword, 2);
        verify(output).println(contains("You picked up 2x Bronze Sword"));
    }

    @Test
    void givenRequestedQuantityExceedsDrop_whenExecute_thenPrintsNotEnoughMessage() {

        TakeCommand cmd = new TakeCommand();
        Item sword = registry.getItem("Bronze Sword");
        when(tile.takeItem(sword, 5)).thenReturn(false);

        cmd.execute(new String[]{"take", "Bronze", "Sword", "5"}, player, state);

        assertEquals(0, player.getInventory().getQuantity(sword));
        verify(output).println(contains("No such item or not enough quantity"));
    }

    @Test
    void givenMissingArguments_whenExecute_thenPrintsUsage() {
        TakeCommand cmd = new TakeCommand();
        cmd.execute(new String[]{"take"}, player, state);
        verify(output).println(contains("Usage"));
    }

    @Test
    void givenNonNumericQuantity_whenExecute_thenPrintsQuantityError() {
        TakeCommand cmd = new TakeCommand();
        cmd.execute(new String[]{"take", "Bronze", "Sword", "abc"}, player, state);
        verify(output).println(contains("Quantity must be a number"));
    }

    @Test
    void givenMapNotLoaded_whenExecute_thenPrintsMapError() {
        TakeCommand cmd = new TakeCommand();
        when(state.getMap()).thenReturn(null);

        cmd.execute(new String[]{"take", "Bronze", "Sword", "1"}, player, state);
        verify(output).println(contains("Map was not loaded"));
    }

    @Test
    void givenUnknownItemName_whenExecute_thenPrintsUnknownItemError() {
        TakeCommand cmd = new TakeCommand();
        cmd.execute(new String[]{"take", "Unknown", "Item", "1"}, player, state);
        verify(output).println(contains("Item unknown"));
    }

    @Test
    void givenNoMatchingDropInTile_whenExecute_thenKeepsInventoryUnchanged() {
        TakeCommand cmd = new TakeCommand();
        Item sword = registry.getItem("Bronze Sword");
        when(tile.takeItem(sword, 1)).thenReturn(false);
        cmd.execute(new String[]{"take", "Bronze", "Sword", "1"}, player, state);
        assertEquals(0, player.getInventory().getQuantity(sword));
        verify(output).println(contains("No such item or not enough quantity"));
    }

    @Test
    void givenThreeWordItemName_whenExecute_thenParsesNameAndTakesItem() {
        TakeCommand cmd = new TakeCommand();
        Item dagger = registry.getItem("Dagger of Shadows");
        when(tile.takeItem(dagger, 1)).thenReturn(true);

        cmd.execute(new String[]{"take", "Dagger","of", "Shadows", "1"}, player, state);
        assertEquals(1, player.getInventory().getQuantity(dagger));
        verify(output).println(contains("You picked up 1x Dagger of Shadows"));
    }
}
