package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TakeCommandTest {
    private Player player;
    private TakeCommand take;
    private Tile tile;
    private Map gameMap;

    @BeforeEach
    void setup() throws Exception {
        // Create singleton Map instance with dummy map
        gameMap = Map.getInstance();
        Tile[][] dummyTiles = new Tile[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                dummyTiles[y][x] = new Tile(TileTypes.GRASS, "Zone", true);
            }
        }

        // Inject dummyTiles into Map
        Field field = Map.class.getDeclaredField("gameMap");
        field.setAccessible(true);
        field.set(gameMap, dummyTiles);

        // Attach map to Game singleton
        Field gameMapField = Game.class.getDeclaredField("gameMap");
        gameMapField.setAccessible(true);
        gameMapField.set(null, gameMap);

        // Create Player
        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,
                GameConstants.PLAYER_START.x(),
                GameConstants.PLAYER_START.y(),
                "Spawn",
                null, // armor
                null, // weapon
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, // quest
                null  // mission
        );
        Game.setPlayer(player);

        // Select a tile
        tile = gameMap.curZone(1, 4);

        // Add your 10 drops
        List<ItemDrop> tileDrops = new ArrayList<>();
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Bronze Sword"), 1));           // drop1
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Dagger of Shadows"), 1));      // drop2
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Mage’s Staff"), 1));           // drop3
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Torn Leather Armor"), 1));     // drop4
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Greater Health Potion"), 2));  // drop6
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Ice Dagger"), 30));     // drop7
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Greater Mana Potion"), 4));    // drop8
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Elixir of Vigor"), 3));     // drop9
        tileDrops.add(new ItemDrop(ItemRegistry.getItem("Traveler’s Leather"), 17));    // drop10

        // Inject into tile
        Field drops = Tile.class.getDeclaredField("drops");
        drops.setAccessible(true);
        drops.set(tile, tileDrops);

        take = new TakeCommand();
        player.move(1,4);
    }

    @Test
    void testTakeBronzeSword() {
        String[] args = {"take", "Bronze", "Sword", "1"};
        take.execute(args);

        assertEquals(1, player.getInventory().getQuantity(ItemRegistry.getItem("Bronze Sword")));
        assertEquals(0, getDropQuantity(tile, "Bronze Sword"));
    }

    @Test
    void testTakeDaggerOfShadowsOne() {
        String[] args = {"take", "Dagger", "of", "Shadows", "1"};
        take.execute(args);

        assertEquals(1, player.getInventory().getQuantity(ItemRegistry.getItem("Dagger of Shadows")));
        assertEquals(0, getDropQuantity(tile, "Dagger of Shadows"));
    }

    @Test
    void testTakeMageStaff() {
        String[] args = {"take", "Mage’s", "Staff", "1"};
        take.execute(args);

        assertEquals(1, player.getInventory().getQuantity(ItemRegistry.getItem("Mage’s Staff")));
        assertEquals(0, getDropQuantity(tile, "Mage’s Staff"));
    }

    @Test
    void testTakeTornLeatherArmor() {
        String[] args = {"take", "Torn", "Leather", "Armor", "1"};
        take.execute(args);

        assertEquals(1, player.getInventory().getQuantity(ItemRegistry.getItem("Torn Leather Armor")));
        assertEquals(0, getDropQuantity(tile, "Torn Leather Armor")); // had 3 total
    }

    @Test
    void testTakeTravelerLeather() {
        String[] args = {"take", "Traveler’s", "Leather", "1"};
        take.execute(args);

        assertEquals(1, player.getInventory().getQuantity(ItemRegistry.getItem("Traveler’s Leather")));
        assertEquals(16, getDropQuantity(tile, "Traveler’s Leather")); // had 17 total
    }

    @Test
    void testTakeGreaterHealthPotion() {
        String[] args = {"take", "Greater", "Health", "Potion", "2"};
        take.execute(args);

        assertEquals(2, player.getInventory().getQuantity(ItemRegistry.getItem("Greater Health Potion")));
        assertEquals(0, getDropQuantity(tile, "Greater Health Potion"));
    }

    @Test
    void testTakeTooManyDaggers() {
        String[] args = {"take", "Ice", "Dagger", "40"}; // more than available
        take.execute(args);

        // no change since too many requested
        assertEquals(0, player.getInventory().getQuantity(ItemRegistry.getItem("Ice Dagger")));
        assertEquals(30, getDropQuantity(tile, "Ice Dagger"));
    }

    @Test
    void testTakeGreaterManaPotion() {
        String[] args = {"take", "Greater", "Mana", "Potion", "4"};
        take.execute(args);

        assertEquals(4, player.getInventory().getQuantity(ItemRegistry.getItem("Greater Mana Potion")));
        assertEquals(0, getDropQuantity(tile, "Greater Mana Potion"));
    }

    @Test
    void testTakeAllElixirOfVigor() {
        String[] args = {"take", "Elixir", "of", "Vigor", "3"}; // all 3 pieces
        take.execute(args);

        assertEquals(3, player.getInventory().getQuantity(ItemRegistry.getItem("Elixir of Vigor")));
        assertEquals(0, getDropQuantity(tile, "Elixir of Vigor"));
    }

    @Test
    void testTakeAllTravelerLeather() {
        String[] args = {"take", "Traveler’s", "Leather", "17"}; // take all
        take.execute(args);

        assertEquals(17, player.getInventory().getQuantity(ItemRegistry.getItem("Traveler’s Leather")));
        assertEquals(0, getDropQuantity(tile, "Traveler’s Leather"));
    }

    private int getDropQuantity(Tile tile, String itemName) {
        return tile.getDrops().stream()
                .filter(d -> d.item().getName().equals(itemName))
                .mapToInt(ItemDrop::quantity)
                .sum();
    }

}
