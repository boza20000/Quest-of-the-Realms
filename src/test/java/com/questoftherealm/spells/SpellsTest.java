package com.questoftherealm.spells;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.InvalidSpellCommand;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;
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

        when(mockState.getMessages()).thenReturn(new LocalizationService());

        GameServices services = mock(GameServices.class);
        when(mockState.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(mockOutput);

        mockChar = mock(Characters.class);
        mockPlayer = mock(Player.class);

        when(mockPlayer.getPlayerCharacter()).thenReturn(mockChar);
        when(mockPlayer.getPlayerType()).thenReturn(PlayerTypes.Mage);

        mockEnemy = mock(Enemy.class);
        spellRegister = new SpellRegister(mockState);
    }


    @Test
    void givenRegisteredSpellName_whenGetSpell_thenReturnsSpell() {
        Spell fireball = spellRegister.getSpell("fireball");
        assertNotNull(fireball);
        assertEquals("Fireball", fireball.getSpellName());
    }

    @Test
    void givenUnknownSpellName_whenGetSpell_thenThrowsInvalidCommand() {
        assertThrows(InvalidSpellCommand.class, () -> spellRegister.getSpell("InvalidSpell"));
    }

    @Test
    void givenSpellRegister_whenListSpells_thenPrintsAvailableSpells() {
        spellRegister.listSpells();
        verify(mockOutput, atLeastOnce()).println(anyString());
    }

    @Test
    void givenEnoughMana_whenCastFireball_thenDamagesEnemyAndConsumesMana() {
        when(mockChar.getMana()).thenReturn(10); // enough mana
        Fireball fireball = new Fireball(mockState);
        fireball.cast(mockPlayer, mockEnemy, mockState);

        verify(mockEnemy).takeDamage(fireball.takePower(), mockState);
        verify(mockPlayer).loseMana(fireball.getManaCost());
        verify(mockOutput, atLeastOnce()).println(contains(fireball.getSymbol()));
    }

    @Test
    void givenEnoughMana_whenCastLightningBolt_thenDamagesEnemyAndConsumesMana() {
        when(mockChar.getMana()).thenReturn(10);

        LightningBolt bolt = new LightningBolt(mockState);//mana cost 7
        bolt.cast(mockPlayer, mockEnemy, mockState);

        verify(mockEnemy).takeDamage(bolt.takePower(), mockState);
        verify(mockPlayer).loseMana(bolt.getManaCost());
        verify(mockOutput, atLeastOnce()).println(contains(bolt.getSymbol()));
    }

    @Test
    void givenLowMana_whenCastDamageSpell_thenPrintsLowManaAndSkipsDamage() {
        Output localOutput = mock(Output.class);
        Input localInput = mock(Input.class);

        GameState realState = new GameState("test-room", new GameServices(localOutput, localInput));
        Player realMage = new Player("MageHero", PlayerTypes.Mage, realState);
        realMage.getPlayerCharacter().setMana(0);

        Fireball fireball = new Fireball(realState);
        fireball.cast(realMage, mockEnemy, realState);

        verify(localOutput).println(contains("Not enough mana"));

    }


}
