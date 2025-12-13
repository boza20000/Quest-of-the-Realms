package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Map;
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
    private Map gameMap;
    private Tile curTile;

    @BeforeEach
    void setup() {
        player = spy(new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,
                GameConstants.Castle.x(),
                GameConstants.Castle.y(),
                "Castle",
                null,
                null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null,
                null,
                false
        ));

        command = spy(new AttackCommand());

        output = mock(Output.class);
        GameServices services = mock(GameServices.class);

        when(services.getOutput()).thenReturn(output);

        state = new GameState(player, services);

        gameMap = mock(Map.class);
        curTile = mock(Tile.class);

        state.setMap(gameMap);
        when(gameMap.curZone(player.getX(), player.getY())).thenReturn(curTile);

        when(curTile.getEnemy(anyString())).thenReturn(null);

        doReturn(true).when(command).playerBaseCheck(player, state);
    }

    @Test
    void makeSafe_Fails_WhenIncorrectArgs() {
        assertFalse(command.makeSafe(new String[]{"attack"}, player, state));
        verify(output).println(anyString());
    }

    @Test
    void makeSafe_Passes_WithTwoArgs() {
        assertTrue(command.makeSafe(new String[]{"attack", "goblin"}, player, state));
    }

    @Test
    void makeSafe_Fails_WhenPlayerCheckFails() {
        doReturn(false).when(command).playerBaseCheck(player, state);
        assertFalse(command.makeSafe(new String[]{"attack", "goblin"}, player, state));
    }

    @Test
    void execute_Stops_WhenMakeSafeFails() {
        doReturn(false).when(command).makeSafe(any(), eq(player), eq(state));
        command.execute(new String[]{"attack", "goblin"}, player, state);
        verify(gameMap, never()).curZone(anyInt(), anyInt());
    }

    @Test
    void execute_PrintsUndefinedArea_WhenTileNull() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(gameMap.curZone(player.getX(), player.getY())).thenReturn(null);
        command.execute(new String[]{"attack", "goblin"}, player, state);
        verify(output).println("You are in an undefined area.");
    }

    @Test
    void execute_PrintsEnemyMissing_WhenEnemyNull() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(curTile.getEnemy("orc")).thenReturn(null);
        command.execute(new String[]{"attack", "orc"}, player, state);
        verify(output).println("No enemy named orc here!");
    }

    @Test
    void execute_PrintsError_WhenInteractThrows() throws Exception {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        doThrow(new RuntimeException("boom")).when(enemy).interact(player, state);

        command.execute(new String[]{"attack", "goblin"}, player, state);

        verify(output).println("Battle was unavailable");
    }

    @Test
    void execute_NoRewards_WhenEnemyNotKilled() throws Exception {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        when(enemy.interact(player, state)).thenReturn(false);

        command.execute(new String[]{"attack", "goblin"}, player, state);

        verify(player, never()).addMoney(anyInt(), eq(state));
        verify(player, never()).addExp(anyInt());
        verify(curTile, never()).removeEnemy(enemy);
    }

    @Test
    void execute_GivesRewards_WhenEnemyKilled() throws Exception {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        when(enemy.interact(player, state)).thenReturn(true);

        command.execute(new String[]{"attack", "goblin"}, player, state);

        verify(player).addMoney(5, state);
        verify(player).addExp(10);
        verify(curTile).removeEnemy(enemy);
        verify(output).println("Successful battle! You receive 5 Gold and you receive 10 XP.");
    }
}
//given<something>_when<something>_then<something>