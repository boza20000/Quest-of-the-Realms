package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.characters.playerCharacters.Warrior;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class UseCommandTest {
    private Player player;
    private Map map;
    private Command useCommand;
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

        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0, 0, 0,
                "Spawn", null, null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null,  // mission
                null
        );
        Game.setPlayer(player);
        useCommand = new CommandFactory().getCommand("use");
    }

    @Test
    void testUseValidItem() throws ItemNotFound {
        Item potion = ItemRegistry.getItem("Health Potion");
        player.getInventory().addItem(potion, 1);
        assertEquals(45, player.getPlayerCharacter().getHealth());
        player.getPlayerCharacter().takeDamage(10);//45-10(8 taken reduction 2)
        assertEquals(37, player.getPlayerCharacter().getHealth());
        useCommand.execute(new String[]{"use", "Health", "Potion"});
        assertFalse(player.getInventory().containsItem(potion), "Potion should be removed after use");
        assertEquals(45, player.getPlayerCharacter().getHealth());//+20 for potion
    }

    @Test
    void testUseInvalidItem() {
        useCommand.execute(new String[]{"use", "Nonexistent"});
        String out = output.toString();
        assertTrue(out.contains("This is not item") || out.contains("Item not found"));
    }

    @Test
    void testUseMissingArgs() {
        useCommand.execute(new String[]{"use"});
        assertTrue(output.toString().contains("Usage"));
    }
}
