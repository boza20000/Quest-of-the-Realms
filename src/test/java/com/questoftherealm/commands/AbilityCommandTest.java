package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.AbilityException;
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

class AbilityCommandTest {

    private AbilityCommand command;
    private Player player;
    private GameState state;
    private Output output;
    private Map gameMap;
    private Tile curTile;

    @BeforeEach
    void setup() {
        command = spy(new AbilityCommand()); // IMPORTANT

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

        output = mock(Output.class);
        var services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);
        state = new GameState(player, services);
        state.setPlayer(player);
        gameMap = mock(Map.class);
        state.setMap(gameMap);

        curTile = mock(Tile.class);
        when(gameMap.curZone(player.getX(), player.getY())).thenReturn(curTile);
        when(curTile.getEnemy(anyString())).thenReturn(null);
        doReturn(true).when(command).playerBaseCheck(player, state);
    }


    @Test
    void makeSafe_Fails_WhenIncorrectArgLength() {
        String[] args = {"super"};
        assertFalse(command.makeSafe(args, player, state));
        verify(output).println(anyString());
    }

    @Test
    void makeSafe_Passes_WithTwoArgs() {
        assertTrue(command.makeSafe(new String[]{"super", "goblin"}, player, state));
    }

    @Test
    void makeSafe_Fails_WhenPlayerCheckFails() {
        when(command.playerBaseCheck(player, state)).thenReturn(false);
        assertFalse(command.makeSafe(new String[]{"super", "goblin"}, player, state));
    }

    @Test
    void execute_Stops_WhenMakeSafeFails() {
        doReturn(false).when(command).makeSafe(any(), eq(player), eq(state));
        command.execute(new String[]{"super", "enemy"}, player, state);
        verify(state.getMap(), never()).curZone(anyInt(), anyInt());
    }

    @Test
    void execute_PrintsMessage_WhenTileIsNull() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(state.getMap().curZone(player.getX(), player.getY())).thenReturn(null);
        command.execute(new String[]{"super", "enemy"}, player, state);
        verify(output).println("You are in an undefined area.");
    }

    @Test
    void execute_PrintsMessage_WhenEnemyNotFound() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(state.getMap().curZone(5, 7)).thenReturn(curTile);
        when(curTile.getEnemy("goblin")).thenReturn(null);
        command.execute(new String[]{"super", "goblin"}, player, state);
        verify(output).println("No enemy named 'goblin' here!");
    }

    @Test
    void execute_ActivatesAbility_WhenEnemyFound() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));
        when(state.getMap().curZone(5, 7)).thenReturn(curTile);

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        Characters character = mock(Characters.class);
        doReturn(character).when(player).getPlayerCharacter();

        command.execute(new String[]{"super", "goblin"}, player, state);
        verify(character).activateAbility(player, enemy, state);
    }

    @Test
    void execute_WrapsExceptions_InAbilityException() {
        doReturn(true).when(command).makeSafe(any(), eq(player), eq(state));

        when(state.getMap().curZone(5, 7)).thenReturn(curTile);

        Enemy enemy = mock(Enemy.class);
        when(curTile.getEnemy("goblin")).thenReturn(enemy);

        Characters character = mock(Characters.class);
        doReturn(character).when(player).getPlayerCharacter();

        doThrow(new RuntimeException("boom"))
                .when(character)
                .activateAbility(player, enemy, state);

        assertThrows(AbilityException.class,
                () -> command.execute(new String[]{"super", "goblin"}, player, state));
    }
}
