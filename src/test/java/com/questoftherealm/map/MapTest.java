package com.questoftherealm.map;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    private Map map;
    private Player mockPlayer;
    private GameState mockState;
    private Output mockOutput;

    @BeforeEach
    void setup() {

        mockPlayer = mock(Player.class);
        mockState = mock(GameState.class);
        mockOutput = mock(Output.class);
        map = new Map(mockState);
        when(mockState.getGameServices()).thenReturn(mock(GameServices.class));
        when(mockState.getGameServices().getOutput()).thenReturn(mockOutput);
    }

    @Test
    void testGetGameMapNotNull() {
        assertNotNull(map.getGameMap());
        assertTrue(map.getGameMap().length > 0);
    }

    @Test
    void testMovePlayerUpdatesCurrentZone() {
        when(mockPlayer.getX()).thenReturn(0);
        when(mockPlayer.getY()).thenReturn(0);

        map.movePlayer(mockPlayer, 0, 0);
        verify(mockPlayer).setCurrentZone(map.getGameMap()[0][0].getDescription());
    }

    @Test
    void testPrintCallsOutput() {
        when(mockPlayer.getX()).thenReturn(0);
        when(mockPlayer.getY()).thenReturn(0);

        map.print(mockPlayer, mockState);
        verify(mockOutput, atLeastOnce()).print(anyString());
        verify(mockOutput, atLeastOnce()).println();
    }

    @Test
    void testCurZoneReturnsCorrectTile() {
        assertEquals(map.getGameMap()[0][0], map.curZone(0,0));
    }
}
