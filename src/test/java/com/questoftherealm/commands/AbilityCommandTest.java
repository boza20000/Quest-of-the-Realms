package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.*;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AbilityCommandTest {

    private Player mockPlayer;
    private Enemy mockEnemy;
    private AbilityCommand abilityCommand;
    private MockedStatic<Game> gameMockedStatic;
    private Tile mockTile;
    private Map mockMap;
    private ByteArrayInputStream input;

    @BeforeEach
    void setup() {
        mockPlayer = mock(Player.class);
        mockEnemy = mock(Enemy.class);
        mockMap = mock(Map.class);
        mockTile = mock(Tile.class);

        gameMockedStatic = mockStatic(Game.class);
        gameMockedStatic.when(Game::getGameMap).thenReturn(mockMap);
        gameMockedStatic.when(Game::getPlayer).thenReturn(mockPlayer);

        when(mockMap.curZone(anyInt(), anyInt())).thenReturn(mockTile);
        when(mockTile.getEnemy(anyString())).thenReturn(mockEnemy);

        abilityCommand = new AbilityCommand();
    }

    @Test
    void testMageAbilityCastsSpell() {
        input = new ByteArrayInputStream("fireball\n".getBytes());
        System.setIn(input);
        Mage mage = spy(new Mage());
        when(mockPlayer.getPlayerCharacter()).thenReturn(mage);
        abilityCommand.execute(new String[]{"ability", "enemy"});
        verify(mage, atLeastOnce()).activateAbility(mockPlayer, mockEnemy);
    }

    @Test
    void testMageAbilityCastsInvalidSpell() {
        input = new ByteArrayInputStream("water\n".getBytes());
        System.setIn(input);

        Mage mage = spy(new Mage());
        when(mockPlayer.getPlayerCharacter()).thenReturn(mage);

        abilityCommand.execute(new String[]{"ability", "enemy"});

        // Verify activateAbility was called (optional)
        verify(mage, atLeastOnce()).activateAbility(mockPlayer, mockEnemy);

        // Verify castSpell was NOT called
        verify(mage, never()).castSpell(any(Player.class), any(), any(Enemy.class));
    }

    @Test
    void testWarriorAbilityDealsDoubleDamage() {
        Warrior warrior = spy(new Warrior());
        when(mockPlayer.getPlayerCharacter()).thenReturn(warrior);
        when(mockPlayer.getWeapon()).thenReturn(createTestWeapon("Bronze Sword"));
        // Stub getAttack() on spy
        doReturn(10).when(warrior).getAttack();

        abilityCommand.execute(new String[]{"ability", "enemy"});
        verify(mockEnemy).takeDamage(20);
    }

    @Test
    void testRogueAbilityAttemptsPickpocket() {
        Rogue rogue = spy(new Rogue());
        when(mockPlayer.getPlayerCharacter()).thenReturn(rogue);

        abilityCommand.execute(new String[]{"ability", "enemy"});
        verify(rogue, atLeastOnce()).activateAbility(mockPlayer, mockEnemy);
    }

    @Test
    void testOrcAbilityDealsDoubleDamage() {
        Orc orc = spy(new Orc());
        when(mockPlayer.getPlayerCharacter()).thenReturn(orc);
        when(mockPlayer.getWeapon()).thenReturn(createTestWeapon("Iron Axe"));
        doReturn(15).when(orc).getAttack();

        abilityCommand.execute(new String[]{"ability", "enemy"});
        verify(mockEnemy).takeDamage(30);
    }

    private Item createTestWeapon(String name) {
        return ItemRegistry.getItem(name);// or constructor your Item supports
    }


    @AfterEach
    void tearDown() {
        gameMockedStatic.close(); // Important!
    }
}
