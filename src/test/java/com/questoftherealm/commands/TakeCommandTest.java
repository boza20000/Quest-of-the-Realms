package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TakeCommandTest {

    private Player player;
    private GameState state;
    private GameServices services;
    private Output output;
    private Map map;
    private Tile tile;
    private ItemRegistry registry;

    @BeforeEach
    void setup() {
        player = new Player("TestHero", PlayerTypes.Warrior, 1, 0, 0, 0, 0,
                "Spawn", null, null, new Inventory(10), null, null, false);

        state = mock(GameState.class);
        services = mock(GameServices.class);
        output = mock(Output.class);
        MessageBundle m  = new MessageBundle();
        LocalizationService localizationService = new LocalizationService();
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(state.getMessages()).thenReturn(localizationService);
        map = mock(Map.class);
        tile = mock(Tile.class);
        when(state.getMap()).thenReturn(map);
        when(map.curZone(anyInt(), anyInt())).thenReturn(tile);
        registry = new ItemRegistry(localizationService);
        when(state.getItemRegistry()).thenReturn(registry);

    }

    @Test
    void takesItemSuccessfully() {
        TakeCommand cmd = new TakeCommand();
        Item sword = registry.getItem("Bronze Sword");

        ItemDrop drop = mock(ItemDrop.class);
        when(drop.item()).thenReturn(sword);
        when(drop.quantity()).thenReturn(3);
        when(tile.getDrops()).thenReturn(List.of(drop));

        cmd.execute(new String[]{"take", "Bronze", "Sword", "2"}, player, state);

        assertEquals(2, player.getInventory().getQuantity(sword));
        verify(tile).removeDrop(sword, 2,state);
        verify(output).println(contains("You picked up 2x Bronze Sword"));
    }

    @Test
    void notEnoughQuantity() {

        TakeCommand cmd = new TakeCommand();
        Item sword = registry.getItem("Bronze Sword");

        ItemDrop drop = mock(ItemDrop.class);
        when(drop.item()).thenReturn(sword);
        when(drop.quantity()).thenReturn(1);
        when(tile.getDrops()).thenReturn(List.of(drop));

        cmd.execute(new String[]{"take", "Bronze", "Sword", "5"}, player, state);

        assertEquals(0, player.getInventory().getQuantity(sword));
        verify(output).println(contains("No such item or not enough quantity"));
    }

    @Test
    void missingArguments() {
        TakeCommand cmd = new TakeCommand();
        cmd.execute(new String[]{"take"}, player, state);
        verify(output).println(contains("Usage"));
    }

    @Test
    void invalidQuantity() {
        TakeCommand cmd = new TakeCommand();
        cmd.execute(new String[]{"take", "Bronze", "Sword", "abc"}, player, state);
        verify(output).println(contains("Quantity must be a number"));
    }

    @Test
    void noMapLoaded() {
        TakeCommand cmd = new TakeCommand();
        when(state.getMap()).thenReturn(null);

        cmd.execute(new String[]{"take", "Bronze", "Sword", "1"}, player, state);
        verify(output).println(contains("Map was not loaded"));
    }

    @Test
    void unknownItem() {
        TakeCommand cmd = new TakeCommand();
        cmd.execute(new String[]{"take", "Unknown", "Item", "1"}, player, state);
        verify(output).println(contains("Item unknown"));
    }

    @Test
    void missingDropInTile() {
        TakeCommand cmd = new TakeCommand();
        Item sword = registry.getItem("Bronze Sword");
        when(tile.getDrops()).thenReturn(List.of());
        cmd.execute(new String[]{"take", "Bronze", "Sword", "1"}, player, state);
        assertEquals(0, player.getInventory().getQuantity(sword));
        verify(output).println(contains("No such item or not enough quantity"));
    }

    @Test
    void handlesThreeWordItems() {
        TakeCommand cmd = new TakeCommand();
        Item dagger = registry.getItem("Dagger of Shadows");
        ItemDrop drop = mock(ItemDrop.class);
        when(drop.item()).thenReturn(dagger);
        when(drop.quantity()).thenReturn(1);
        when(tile.getDrops()).thenReturn(List.of(drop));

        cmd.execute(new String[]{"take", "Dagger","of", "Shadows", "1"}, player, state);
        assertEquals(1, player.getInventory().getQuantity(dagger));
        verify(output).println(contains("You picked up 1x Dagger of Shadows"));
    }
}
