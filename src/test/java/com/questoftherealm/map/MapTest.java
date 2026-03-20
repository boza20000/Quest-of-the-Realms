package com.questoftherealm.map;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    private WorldMap map;
    private Player mockPlayer;
    private GameState mockState;
    private Output mockOutput;

    @BeforeEach
    void setup() {

        mockPlayer = mock(Player.class);
        mockState = mock(GameState.class);
        mockOutput = mock(Output.class);
        map = new WorldMap(mockState);
        when(mockState.getGameServices()).thenReturn(mock(GameServices.class));
        when(mockState.getGameServices().getOutput()).thenReturn(mockOutput);
        when(mockState.getActivePlayers()).thenReturn(List.of(mockPlayer));
    }

    @Test
    void givenWorldMap_whenGetGameMap_thenReturnsNonEmptyMatrix() {
        assertNotNull(map.getGameMap());
        assertTrue(map.getGameMap().length > 0);
    }

    @Test
    void givenPlayerCoordinates_whenMovePlayer_thenUpdatesCurrentZoneDescription() {
        when(mockPlayer.getX()).thenReturn(0);
        when(mockPlayer.getY()).thenReturn(0);

        map.movePlayer(mockPlayer, 0, 0);
        verify(mockPlayer).setCurrentZone(map.getGameMap()[0][0].getDescription());
    }

    @Test
    void givenPlayerPosition_whenPrint_thenWritesToOutput() {
        when(mockPlayer.getX()).thenReturn(0);
        when(mockPlayer.getY()).thenReturn(0);

        map.print(mockState);
        verify(mockOutput, atLeastOnce()).print(anyString());
    }

    @Test
    void givenCoordinates_whenCurZone_thenReturnsExpectedTile() {
        assertEquals(map.getGameMap()[0][0], map.curZone(0,0));
    }
}
