package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.AbilityException;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;

import com.questoftherealm.map.Tile;
import com.questoftherealm.map.WorldMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbilityCommandTest {

    private AbilityCommand command;
    private Player player;
    private GameState state;
    private Output output;
    private WorldMap gameMap;
    private Tile curTile;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        GameServices services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);

        state = new GameState("test-room", services);
        player = spy(new Player("TestHero", PlayerTypes.Warrior, state));
        state.addPlayer(player);
        command = spy(new AbilityCommand());
        gameMap = mock(WorldMap.class);
        state.setMap(gameMap);
        curTile = mock(Tile.class);

        when(gameMap.curZone(player.getX(), player.getY())).thenReturn(curTile);
        when(curTile.getEnemy(anyString())).thenReturn(null);
        doReturn(true).when(command).playerBaseCheck(player, state);

    }


    @Test
    void givenInvalidArgLength_whenMakeSafe_thenReturnsFalse() {
        String[] args = {"super"};
        assertFalse(command.makeSafe(args, player, state));
        verify(output).println(anyString());
    }

    @Test
    void givenTwoArgs_whenMakeSafe_thenReturnsTrue() {
        assertTrue(command.makeSafe(new String[]{"super", "goblin"}, player, state));
    }

    @Test
    void givenPlayerCheckFails_whenMakeSafe_thenReturnsFalse() {
        when(command.playerBaseCheck(player, state)).thenReturn(false);
        assertFalse(command.makeSafe(new String[]{"super", "goblin"}, player, state));
    }

    @Test
    void givenUnsafeInput_whenExecute_thenStopsBeforeMapLookup() {
        doReturn(false).when(command).makeSafe(any(), eq(player), eq(state));
        command.execute(new String[]{"super", "enemy"}, player, state);
        verify(state.getMap(), never()).curZone(anyInt(), anyInt());
    }

    @Test
    void givenNullTile_whenExecute_thenPrintsUndefinedArea() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(state.getMap().curZone(player.getX(), player.getY())).thenReturn(null);
        command.execute(new String[]{"super", "enemy"}, player, state);
        verify(output).println("You are in an undefined area.");
    }

    @Test
    void givenEnemyMissing_whenExecute_thenPrintsMissingEnemyMessage() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(state.getMap().curZone(anyInt(), anyInt())).thenReturn(curTile);
        when(curTile.getEnemy("goblin")).thenReturn(null);
        command.execute(new String[]{"super", "goblin"}, player, state);
        verify(output).println("No enemy named goblin here!");
    }

    @Test
    void givenEnemyFound_whenExecute_thenActivatesAbility() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(state.getMap().curZone(anyInt(), anyInt())).thenReturn(curTile);

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        Characters character = mock(Characters.class);
        doReturn(character).when(player).getPlayerCharacter();

        command.execute(new String[]{"super", "goblin"}, player, state);
        verify(character).activateAbility(player, enemy, state);
    }

    @Test
    void givenAbilityThrows_whenExecute_thenWrapsInAbilityException() {
        AbilityCommand realCommand = new AbilityCommand();

        Output testOutput = mock(Output.class);
        Input testInput = mock(Input.class);
        when(testInput.nextLine()).thenReturn("not-a-real-spell");

        GameServices testServices = mock(GameServices.class);
        when(testServices.getOutput()).thenReturn(testOutput);
        when(testServices.getInput()).thenReturn(testInput);

        GameState testState = new GameState("test-room", testServices);
        Player mage = new Player("MageHero", PlayerTypes.Mage, testState);
        testState.addPlayer(mage);

        WorldMap testMap = mock(WorldMap.class);
        Tile testTile = mock(Tile.class);
        Enemy enemy = mock(Enemy.class);

        when(testMap.curZone(anyInt(), anyInt())).thenReturn(testTile);
        when(testTile.getEnemy("goblin")).thenReturn(enemy);
        testState.setMap(testMap);

        assertThrows(AbilityException.class, () -> realCommand.execute(new String[]{"super", "goblin"}, mage, testState));
        verify(testInput).nextLine();
    }
}
