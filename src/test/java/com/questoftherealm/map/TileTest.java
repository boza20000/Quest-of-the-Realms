package com.questoftherealm.map;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.Loot;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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
    void givenPlayerEntersTile_whenOnEnter_thenContentIsGenerated() {
       LocalizationService mockL =  mock(LocalizationService.class);
       when(mockState.getMessages()).thenReturn(mockL);
       when(mockL.getBundle()).thenReturn(new MessageBundle());
        tile.onEnter(mockPlayer, mockState);
        assertTrue(tile.isContentGenerated());
        verify(mockOutput, atLeastOnce()).println();
    }

    @Test
    void givenNewTile_whenReadingProperties_thenReturnsConfiguredValues() {
        assertEquals(TileTypes.GRASS, tile.getType());
        assertEquals("Grassland", tile.getDescription());
        assertTrue(tile.isWalkable());
    }

    @Test
    void givenDropExists_whenPickItem_thenReturnsAndRemovesDrop() {

        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Potion");

        ItemDrop dropToAdd = new ItemDrop(mockItem, 1);
        tile.addDrop(dropToAdd);

        assertFalse(tile.getDrops().isEmpty());

        ItemDrop picked = tile.pickItem("Potion");

        assertNotNull(picked);
        assertEquals(mockItem, picked.item());
        assertEquals(1, picked.quantity());
        assertTrue(tile.getDrops().isEmpty());
    }

    @Test
    void givenEnemyPresent_whenRemoveEnemy_thenEnemyListBecomesEmpty() {
        Enemy enemy = mock(Enemy.class);
        when(enemy.getLoot()).thenReturn(List.of());
        
        tile.addEnemy(enemy);
        assertFalse(tile.getEnemies().isEmpty());

        tile.removeEnemy(enemy,mockState);
        assertTrue(tile.getEnemies().isEmpty());
    }

    @Test
    void givenEnemyWithGuaranteedLoot_whenRemoveEnemy_thenLootIsAddedToTile() {
        Enemy enemy = mock(Enemy.class);
        Item lootItem = mock(Item.class);
        when(enemy.getLoot()).thenReturn(List.of(new Loot(lootItem, 1.0, 1, 1)));

        tile.addEnemy(enemy);
        tile.removeEnemy(enemy, mockState);

        List<ItemDrop> drops = tile.getDrops();
        assertEquals(1, drops.size());
        assertSame(lootItem, drops.getFirst().item());
        assertEquals(1, drops.getFirst().quantity());
    }

    @Test
    void givenNpcRegistered_whenGetNpcById_thenReturnsNpc() {
        var npc = mock(Npc.class);
        when(npc.getId()).thenReturn("npc1");

        tile.registerNpc(npc);
        assertEquals(npc, tile.getNpcById("npc1"));
    }

    @Test
    void givenNoDropsOrEnemies_whenIsEmpty_thenReturnsTrue() {
        tile.getDrops().clear();
        tile.getEnemies().clear();
        assertTrue(tile.isEmpty());
    }
}
