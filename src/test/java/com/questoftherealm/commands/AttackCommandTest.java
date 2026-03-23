package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.WorldMap;
import com.questoftherealm.map.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttackCommandTest {

    private AttackCommand command;
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

        command = spy(new AttackCommand());

        gameMap = mock(WorldMap.class);
        curTile = mock(Tile.class);

        state.setMap(gameMap);
        when(gameMap.curZone(player.getX(), player.getY())).thenReturn(curTile);

        when(curTile.getEnemy(anyString())).thenReturn(null);

        doReturn(true).when(command).playerBaseCheck(player, state);
    }

    @Test
    void givenInvalidArgs_whenMakeSafe_thenReturnsFalse() {
        assertFalse(command.makeSafe(new String[]{"attack"}, player, state));
        verify(output).println(anyString());
    }

    @Test
    void givenTwoArgs_whenMakeSafe_thenReturnsTrue() {
        assertTrue(command.makeSafe(new String[]{"attack", "goblin"}, player, state));
    }

    @Test
    void givenPlayerCheckFails_whenMakeSafe_thenReturnsFalse() {
        doReturn(false).when(command).playerBaseCheck(player, state);
        assertFalse(command.makeSafe(new String[]{"attack", "goblin"}, player, state));
    }

    @Test
    void givenUnsafeInput_whenExecute_thenStopsBeforeMapLookup() {
        doReturn(false).when(command).makeSafe(any(), eq(player), eq(state));
        command.execute(new String[]{"attack", "goblin"}, player, state);
        verify(gameMap, never()).curZone(anyInt(), anyInt());
    }

    @Test
    void givenNullTile_whenExecute_thenPrintsUndefinedArea() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(gameMap.curZone(player.getX(), player.getY())).thenReturn(null);
        command.execute(new String[]{"attack", "goblin"}, player, state);
        verify(output).println("You are in an undefined area.");
    }

    @Test
    void givenMissingEnemy_whenExecute_thenPrintsEnemyMissing() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(curTile.getEnemy("orc")).thenReturn(null);
        command.execute(new String[]{"attack", "orc"}, player, state);
        verify(output).println("No enemy named orc here!");
    }

    @Test
    void givenBattleThrows_whenExecute_thenPrintsUnavailableBattle() throws Exception {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        doThrow(new RuntimeException("boom")).when(enemy).interact(player, state);

        command.execute(new String[]{"attack", "goblin"}, player, state);

        verify(output).println("Battle with goblin was unavailable");
    }

    @Test
    void givenEnemySurvives_whenExecute_thenNoRewardsAreGranted() throws Exception {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        when(enemy.interact(player, state)).thenReturn(false);

        command.execute(new String[]{"attack", "goblin"}, player, state);

        verify(player, never()).addMoney(anyInt(), eq(state));
        verify(player, never()).addExp(anyInt());
        verify(curTile, never()).removeEnemy(enemy,state);
    }

    @Test
    void givenEnemyDies_whenExecute_thenRewardsAndRemovalAreApplied() throws Exception {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        when(enemy.interact(player, state)).thenReturn(true);

        command.execute(new String[]{"attack", "goblin"}, player, state);

        verify(player).addMoney(5, state);
        verify(player).addExp(20);
        verify(curTile).removeEnemy(enemy,state);
        verify(output).println(anyString());
    }
}

