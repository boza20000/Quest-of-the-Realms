package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class MoveCommandTest {
    private Player player;
    private MoveCommand moveCommand;
    private Map map;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setup() throws Exception {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        map = Map.getInstance();
        Tile[][] tiles = new Tile[8][8];
        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++)
                tiles[y][x] = new Tile(TileTypes.GRASS, "Zone " + x + "," + y, true);

        Field f = Map.class.getDeclaredField("gameMap");
        f.setAccessible(true);
        f.set(map, tiles);

        Field gf = Game.class.getDeclaredField("gameMap");
        gf.setAccessible(true);
        gf.set(null, map);

        player = new Player("TestHero", PlayerTypes.Warrior,
                1, 0, 0,  GameConstants.PLAYER_START.x(),
                GameConstants.PLAYER_START.y(),
                "Spawn", null, null, new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null);
        Game.setPlayer(player);
        moveCommand = new MoveCommand();
    }

    @Test
    void testMoveNorth() {
        moveCommand.execute(new String[]{"move", "north"});
        assertEquals(5, player.getY(), "Player should move north (y-1)");
    }
    @Test
    void testMoveSouth() {
        moveCommand.execute(new String[]{"move", "south"});
        assertEquals(7, player.getY(), "Player should move north (y-1)");
    }
    @Test
    void testMoveEast() {
        moveCommand.execute(new String[]{"move", "east"});
        assertEquals(2, player.getX(), "Player should move north (y-1)");
    }
    @Test
    void testMoveWest() {
        moveCommand.execute(new String[]{"move", "west"});
        assertEquals(0, player.getX(), "Player should move north (y-1)");
    }

    @Test
    void testInvalidDirection() {
        moveCommand.execute(new String[]{"move", "up"});
        assertTrue(output.toString().contains("Invalid direction"));
    }

    @Test
    void testMissingArgs() {
        moveCommand.execute(new String[]{"move"});
        assertTrue(output.toString().contains("Usage"));
    }
}
