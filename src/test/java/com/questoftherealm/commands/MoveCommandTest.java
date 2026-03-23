package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.WorldMap;
import com.questoftherealm.map.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MoveCommandTest {

    private Player player;
    private MoveCommand moveCommand;
    private GameState state;
    private Output output;
    private WorldMap map;
    private Tile currentTile;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        GameServices services = mock(GameServices.class);
        when(services.getOutput()).thenReturn(output);

        GameState s = new GameState("test-room",services);
        state = spy(s);
        player = spy(new Player("TestHero", PlayerTypes.Warrior, state));
        state.addPlayer(player);
        when(state.getGameServices()).thenReturn(services);

        map = mock(WorldMap.class);
        currentTile = mock(Tile.class);
        when(map.curZone(anyInt(), anyInt())).thenReturn(currentTile);
        when(state.getMap()).thenReturn(map);
        state.setSimulation(true);

        moveCommand = new MoveCommand();
    }

    @Test
    void givenNorthDirection_whenExecute_thenDecrementsY() {
        int startY = player.getY();
        moveCommand.execute(new String[]{"move", "north"}, player, state);
        assertEquals(startY - 1, player.getY());
    }

    @Test
    void givenSouthDirection_whenExecute_thenIncrementsY() {
        int startY = player.getY();
        moveCommand.execute(new String[]{"move", "south"}, player, state);
        assertEquals(startY + 1, player.getY());
    }

    @Test
    void givenEastDirection_whenExecute_thenIncrementsX() {
        int startX = player.getX();
        moveCommand.execute(new String[]{"move", "east"}, player, state);
        assertEquals(startX + 1, player.getX());
    }

    @Test
    void givenWestDirection_whenExecute_thenDecrementsX() {
        int startX = player.getX();
        moveCommand.execute(new String[]{"move", "west"}, player, state);
        assertEquals(startX - 1, player.getX());
    }

    @Test
    void givenInvalidDirection_whenExecute_thenPrintsError() {
        moveCommand.execute(new String[]{"move", "up"}, player, state);
        verify(output,atLeastOnce()).print(anyString());
    }

    @Test
    void givenMissingArguments_whenExecute_thenPrintsUsage() {
        moveCommand.execute(new String[]{"move"}, player, state);
        verify(output).println("Usage: " + moveCommand.getDescription(state));
    }
}
