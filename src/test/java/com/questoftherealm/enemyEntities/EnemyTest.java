package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.entities.Goblin;
import com.questoftherealm.game.ConsoleInput;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnemyTest {

    private Player player;
    private Enemy enemy;
    private GameState state;
    private LocalizationService localizationService;

    @BeforeEach
    void setup() {
        player = mock(Player.class);
        Output output = new ConsoleOutput();
        Input input = new ConsoleInput();
        state = new GameState("test-room", new GameServices(output,input));
        enemy = new Goblin(state,new EnemyConstants());
        localizationService = new LocalizationService();
    }

    @Test
    void givenNewGoblin_whenConstructed_thenDefaultStatsAndLootAreInitialized() {
        assertEquals(localizationService.getBundle().get("enemy.goblin.desc"), enemy.getDescription());
        assertEquals(EnemyType.GOBLIN, enemy.getType());
        assertEquals(30, enemy.getHealth());
        assertEquals(5, enemy.getBaseAttack());
        assertEquals(2, enemy.getBaseDefense());
        assertNotNull(enemy.getArmor());
        assertNotNull(enemy.getWeapon());
        assertFalse(enemy.isDead());
        assertNotNull(enemy.getLoot());
    }

    @Test
    void givenUpdatedFields_whenSettersCalled_thenValuesAreApplied() {
        enemy.setHealth(20);
        assertEquals(20, enemy.getHealth());

        Item weapon = mock(Item.class);
        enemy.setWeapon(weapon);
        assertEquals(weapon, enemy.getWeapon());

        List<Item> armorList = List.of(mock(Item.class));
        enemy.setArmor(armorList);
        assertEquals(armorList, enemy.getArmor());
    }

    @Test
    void givenIncomingDamage_whenTakeDamage_thenHealthDropsAndDeathStateUpdates() {
        int startHealth = enemy.getHealth();
        int damage = 10;
        enemy.takeDamage(damage, state);
        int armor = enemy.getArmor().stream().mapToInt(Item::getPower).sum();
        int defence = enemy.getBaseDefense();
        assertEquals(startHealth - (damage - (armor + defence) / 2), enemy.getHealth());
        assertFalse(enemy.isDead());

        enemy.takeDamage(100, state);
        assertEquals(0, enemy.getHealth());
        assertTrue(enemy.isDead());
    }

    @Test
    void givenHealthChanges_whenIsAliveChecked_thenReflectsHealthState() {
        assertTrue(enemy.isAlive());
        enemy.setHealth(0);
        assertFalse(enemy.isAlive());
    }

    @Test
    void givenTargetPlayer_whenAttack_thenDealsDamageToCharacter() {
        var mockCharacter = mock(Characters.class);
        when(player.getPlayerCharacter()).thenReturn(mockCharacter);
        enemy.attack(player, state);
        verify(mockCharacter, atLeastOnce()).takeDamage(anyInt(), eq(state), eq(player));
    }

    @Test
    void givenGrassTileType_whenGenerateEnemies_thenCreatesBoundedEnemyList() {
        List<Enemy> enemies = Enemy.generateEnemies(TileTypes.GRASS,state);
        assertNotNull(enemies);
        for (Enemy e : enemies) {
            assertNotNull(e.getType());
            assertNotNull(e.getDescription());
        }
        assertTrue(enemies.size() < 4, "max enemies per tile is 3");
    }
}
