package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SpellsTest {

    private Output mockOutput;
    private GameState mockState;
    private Player mockPlayer;
    private Characters mockChar;
    private Enemy mockEnemy;

    private SpellRegister spellRegister;

    @BeforeEach
    void setup() {
        mockOutput = mock(Output.class);
        mockState = mock(GameState.class);
        when(mockState.getGameServices()).thenReturn(mock(GameServices.class));
        when(mockState.getGameServices().getOutput()).thenReturn(mockOutput);

        mockPlayer = mock(Player.class);
        mockChar = mock(Characters.class);
        when(mockPlayer.getPlayerCharacter()).thenReturn(mockChar);
        when(mockPlayer.getPlayerType()).thenReturn(PlayerTypes.Mage);

        mockEnemy = mock(Enemy.class);

        spellRegister = new SpellRegister(mockOutput);
    }

    @Test
    void testGetSpellValid() {
        Spell fireball = spellRegister.getSpell("fireball");
        assertNotNull(fireball);
        assertEquals("Fireball", fireball.getSpellName());
    }

    @Test
    void testGetSpellInvalid() {
        assertThrows(InvalidCommand.class, () -> spellRegister.getSpell("InvalidSpell"));
    }

    @Test
    void testListSpellsPrintsOutput() {
        spellRegister.listSpells();
        verify(mockOutput, atLeastOnce()).println(anyString());
    }

    @Test
    void testFireballCastReducesEnemyHealthAndPlayerMana() {
        when(mockChar.getMana()).thenReturn(10); // enough mana
        doNothing().when(mockEnemy).takeDamage(anyInt(), eq(mockState));
        doAnswer(invocation -> {
            when(mockChar.getMana()).thenReturn(5); // simulate mana loss
            return null;
        }).when(mockPlayer).loseMana(anyInt());

        Fireball fireball = new Fireball();
        fireball.cast(mockPlayer, mockEnemy, mockState);

        verify(mockEnemy).takeDamage(fireball.takePower(), mockState);
        verify(mockPlayer).loseMana(fireball.getManaCost());
        verify(mockOutput, atLeastOnce()).println(contains(fireball.getSpellName()));
    }

    @Test
    void testLightningBoltCastReducesEnemyHealthAndPlayerMana() {
        when(mockChar.getMana()).thenReturn(10);
        doNothing().when(mockEnemy).takeDamage(anyInt(), eq(mockState));
        doAnswer(invocation -> {
            when(mockChar.getMana()).thenReturn(3);
            return null;
        }).when(mockPlayer).loseMana(anyInt());

        LightningBolt bolt = new LightningBolt(mockState);
        bolt.cast(mockPlayer, mockEnemy, mockState);

        verify(mockEnemy).takeDamage(bolt.takePower(), mockState);
        verify(mockPlayer).loseMana(bolt.getManaCost());
        verify(mockOutput, atLeastOnce()).println(contains(bolt.getSpellName()));
    }

    @Test
    void testDamageSpellLowMana() {
        when(mockChar.getMana()).thenReturn(1); // less than Fireball manaCost

        Fireball fireball = new Fireball(mockState);
        fireball.cast(mockPlayer, mockEnemy, mockState);

        verify(mockOutput).println(contains("💤")); // low mana warning
        verify(mockEnemy, never()).takeDamage(anyInt(), any());
        verify(mockPlayer, never()).loseMana(anyInt());
    }

    @Test
    void testDamageSpellWrongPlayerType() {
        when(mockPlayer.getPlayerType()).thenReturn(PlayerTypes.Warrior);
        when(mockChar.getMana()).thenReturn(10);

        Fireball fireball = new Fireball(mockState);
        fireball.cast(mockPlayer, mockEnemy, mockState);

        verify(mockOutput).println(contains("🚫")); // player type error
        verify(mockEnemy).takeDamage(fireball.takePower(), mockState);
        verify(mockPlayer).loseMana(fireball.getManaCost());
    }
}
