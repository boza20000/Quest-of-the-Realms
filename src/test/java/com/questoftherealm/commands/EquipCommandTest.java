package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class EquipCommandTest {

    private Player player;
    private EquipCommand equipCommand;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setup() {
        // Setup player with inventory
        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,  // Level, XP, gold
                0, 0,     // Position
                "Spawn",
                null, null,
                new Inventory(10),
                null, null,
                null
        );
        Game.setPlayer(player);
        equipCommand = new EquipCommand();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testEquipExistingItem() {
        Item sword = ItemRegistry.getItem("Bronze Sword");
        player.getInventory().addItem(sword,1);
        String[] args = {"equip", "Bronze Sword"};
        equipCommand.execute(args);
        assertEquals(sword, player.getWeapon());
        assertTrue(outContent.toString().contains("Bronze Sword has been equipped successfully!"));
    }

    @Test
    void testEquipNonExistingItemInInventory() {
        String[] args = {"equip", "Bronze Sword"};
        equipCommand.execute(args);
        assertNotNull(player.getWeapon());
        assertTrue(outContent.toString().contains("You don't have 'Bronze Sword' in your inventory."));
    }

    @Test
    void testEquipInvalidItemName() {
        String[] args = {"equip", "Mystic Wand"};
        equipCommand.execute(args);
        assertNotNull(player.getWeapon());
        assertTrue(outContent.toString().contains("This item doesn't exist."));
    }

    @Test
    void testEquipMissingArgument() {
        String[] args = {"equip"};
        equipCommand.execute(args);
        assertTrue(outContent.toString().contains("Usage: equip [item name] — equips an item from your inventory if available"));
    }
}
