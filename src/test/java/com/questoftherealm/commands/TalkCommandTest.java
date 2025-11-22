package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.mockito.Mockito.*;

class TalkCommandTest {

    private Player player;
    private GameState state;
    private GameServices services;
    private Output output;
    private Map map;
    private Tile tile;

    @BeforeEach
    void setup() {
        player = new Player("TestHero", PlayerTypes.Warrior);

        state = mock(GameState.class);
        services = mock(GameServices.class);
        output = mock(Output.class);
        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);

        map = mock(Map.class);
        tile = mock(Tile.class);
        when(state.getMap()).thenReturn(map);
        when(map.curZone(anyInt(), anyInt())).thenReturn(tile);
    }

    @Test
    void testTalkNpcNearby() {
        TalkCommand cmd = new TalkCommand();
        Npc npc = mock(Npc.class);
        when(tile.getNpcByType(NpcType.ELDER)).thenReturn(npc);

        cmd.execute(new String[]{"talk", "elder"}, player, state);

        verify(npc).talk(state, player, false);
    }

    @Test
    void testTalkUnknownNpc() {
        TalkCommand cmd = new TalkCommand();
        cmd.execute(new String[]{"talk", "ghost"}, player, state);
        verify(output).println(contains("Unknown NPC"));
    }

    @Test
    void testTalkNpcNotNearby() {
        TalkCommand cmd = new TalkCommand();
        when(tile.getNpcByType(NpcType.ELDER)).thenReturn(null);

        cmd.execute(new String[]{"talk", "elder"}, player, state);
        verify(output).println(contains("No elder nearby"));
    }

    @Test
    void testTalkCommandMissingArgs() {
        TalkCommand cmd = new TalkCommand();
        cmd.execute(new String[]{"talk"}, player, state);
        verify(output).println(contains("Usage"));
    }
}
