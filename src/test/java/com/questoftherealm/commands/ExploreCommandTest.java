package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.map.Locations;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.WorldMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ExploreCommandTest {

    private ExploreCommand command;
    private GameState state;
    private GameServices services;
    private Player player;
    private Output output;
    private LocalizationService localizationService;
    private MessageBundle bundle;
    private WorldMap map;

    @BeforeEach
    void setUp() {
        command = new ExploreCommand();
        state = mock(GameState.class);
        services = mock(GameServices.class);
        player = mock(Player.class);
        output = mock(Output.class);
        localizationService = mock(LocalizationService.class);
        bundle = mock(MessageBundle.class);
        map = mock(WorldMap.class);

        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        when(state.getMessages()).thenReturn(localizationService);
        when(localizationService.getBundle()).thenReturn(bundle);
        when(state.getMap()).thenReturn(map);

        when(bundle.get(anyString())).thenAnswer(inv -> inv.getArgument(0));
        when(bundle.get(anyString(), any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void givenInsufficientArgs_whenExecute_thenShowsUsage() {
        String[] args = {"explore"};

        command.execute(args, player, state);
        verify(output).println(contains("explore.usage"));
        verify(player, never()).exploreStructure(anyString(), any());
    }

    @Test
    void givenUndefinedArea_whenExecute_thenShowsUndefinedAreaError() {
        String[] args = {"explore", "Tower"};

        when(map.curZone(anyInt(), anyInt())).thenReturn(null);
        command.execute(args, player, state);
        verify(output).println(contains("explore.error.undefinedArea"));
    }

    @Test
    void givenTileScanningNoStructure_whenExecute_thenShowsUnavailableError() {
        String[] args = {"explore", "Tower"};
        Tile tile = mock(Tile.class);

        when(map.curZone(anyInt(), anyInt())).thenReturn(tile);
        when(tile.getStructure()).thenReturn(null);
        command.execute(args, player, state);
        verify(output).println(contains("explore.error.unavailable"));
    }

    @Test
    void givenStructureNameMismatch_whenExecute_thenShowsNameMismatchError() {
        String[] args = {"explore", "WrongName"};
        Tile tile = mock(Tile.class);
        Locations structure = mock(Locations.class);
        
        when(map.curZone(anyInt(), anyInt())).thenReturn(tile);
        when(tile.getStructure()).thenReturn(structure);
        when(structure.getName()).thenReturn("location.key");
        when(bundle.get("location.key")).thenReturn("Tower");
        command.execute(args, player, state);
        verify(output).println(contains("explore.error.nameMismatch"));
        verify(player, never()).exploreStructure(anyString(), any());
    }

    @Test
    void givenValidStructure_whenExecute_thenPlayerExplores() {
        String[] args = {"explore", "Great", "Tower"}; 
        Tile tile = mock(Tile.class);
        Locations structure = mock(Locations.class);
        
        when(map.curZone(anyInt(), anyInt())).thenReturn(tile);
        when(tile.getStructure()).thenReturn(structure);
        when(structure.getName()).thenReturn("location.key");
        when(bundle.get("location.key")).thenReturn("Great Tower");
        command.execute(args, player, state);
        verify(player).exploreStructure(eq("Great Tower"), eq(state));
    }
    
    @Test
    void givenExceptionDuringProcess_whenExecute_thenCatchAndShowUnavailableError() {
         String[] args = {"explore", "Tower"};
         Tile tile = mock(Tile.class);

         when(map.curZone(anyInt(), anyInt())).thenReturn(tile);
         when(tile.getStructure()).thenThrow(new IllegalArgumentException("Simulated error"));
         command.execute(args, player, state);
         verify(output).println(contains("explore.error.unavailable"));
    }
}

