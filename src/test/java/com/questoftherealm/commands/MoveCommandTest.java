package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Map;
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
    private Map map;
    private Tile currentTile;

    @BeforeEach
    void setup() {
        player =   player = spy(new Player(
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
        state = mock(GameState.class);
        output = mock(Output.class);
        GameServices services = mock(GameServices.class);
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);

        map = mock(Map.class);
        currentTile = mock(Tile.class);
        when(map.curZone(anyInt(), anyInt())).thenReturn(currentTile);
        when(state.getMap()).thenReturn(map);
        state.setSimulation(true);

        moveCommand = new MoveCommand();
    }

    @Test
    void execute_MoveNorth_UpdatesPlayerY() {
        int startY = player.getY();
        moveCommand.execute(new String[]{"move", "north"}, player, state);
        assertEquals(startY - 1, player.getY());
    }

    @Test
    void execute_MoveSouth_UpdatesPlayerY() {
        int startY = player.getY();
        moveCommand.execute(new String[]{"move", "south"}, player, state);
        assertEquals(startY + 1, player.getY());
    }

    @Test
    void execute_MoveEast_UpdatesPlayerX() {
        int startX = player.getX();
        moveCommand.execute(new String[]{"move", "east"}, player, state);
        assertEquals(startX + 1, player.getX());
    }

    @Test
    void execute_MoveWest_UpdatesPlayerX() {
        int startX = player.getX();
        moveCommand.execute(new String[]{"move", "west"}, player, state);
        assertEquals(startX - 1, player.getX());
    }

    @Test
    void execute_InvalidDirection_PrintsError() {
        moveCommand.execute(new String[]{"move", "up"}, player, state);
        verify(output,atLeastOnce()).print(anyString());
    }

    @Test
    void execute_MissingArguments_PrintsUsage() {
        moveCommand.execute(new String[]{"move"}, player, state);
        verify(output).println("Usage: " + moveCommand.getDescription());
    }
}
