package com.questoftherealm.map;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    private Tile tile;
    private GameState mockState;
    private Player mockPlayer;
    private Output mockOutput;

    @BeforeEach
    void setup() {
        tile = new Tile(TileTypes.GRASS, "Grassland", true);
        mockState = mock(GameState.class);
        mockPlayer = mock(Player.class);
        mockOutput = mock(Output.class);


        GameServices mockServices = mock(GameServices.class);
        when(mockState.getGameServices()).thenReturn(mockServices);
        when(mockServices.getOutput()).thenReturn(mockOutput);

        RandomService mockRandom = mock(RandomService.class);
        when(mockServices.getRandom()).thenReturn(mockRandom);
        when(mockRandom.randomInt(anyInt())).thenReturn(0);
        when(mockRandom.random()).thenReturn(new Random());
    }

    @Test
    void testGenerateContentMarksContentGenerated() {
        tile.onEnter(mockPlayer, mockState);
        assertTrue(tile.isContentGenerated());
        verify(mockOutput, atLeastOnce()).println();
    }

    @Test
    void testTileBasicProperties() {
        assertEquals(TileTypes.GRASS, tile.getType());
        assertEquals("Grassland", tile.getDescription());
        assertTrue(tile.isWalkable());
    }

    @Test
    void testPickItemReturnsItemAndRemovesIt() {
        // Mock an item
        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Potion");

        // Add drop to tile
        ItemDrop dropToAdd = new ItemDrop(mockItem, 1);
        tile.getDrops().add(dropToAdd);

        assertFalse(tile.getDrops().isEmpty());

        // Pick the item
        ItemDrop picked = tile.pickItem("Potion");

        // Verify it returns the correct drop
        assertNotNull(picked);
        assertEquals(mockItem, picked.item());
        assertEquals(1, picked.quantity());

        // Verify it was removed from the tile
        assertTrue(tile.getDrops().isEmpty());
    }

    @Test
    void testRemoveEnemy() {
        Enemy enemy = mock(Enemy.class);
        tile.getEnemies().add(enemy);
        assertFalse(tile.getEnemies().isEmpty());

        tile.removeEnemy(enemy);
        assertTrue(tile.getEnemies().isEmpty());
    }

    @Test
    void testRegisterAndRetrieveNpc() {
        var npc = mock(Npc.class);
        when(npc.getId()).thenReturn("npc1");

        tile.registerNpc(npc);
        assertEquals(npc, tile.getNpcById("npc1"));
    }

    @Test
    void testIsEmptyReturnsTrueWhenNoDropsOrEnemies() {
        tile.getDrops().clear();
        tile.getEnemies().clear();

        assertTrue(tile.isEmpty());
    }
}
