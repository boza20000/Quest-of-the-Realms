package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemRegistry;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StatsCommandTest {
    private Player player;
    private StatsCommand statsCommand;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setup() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        HashMap<ItemEffect, Item> armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, ItemRegistry.getItem("Traveler’s Hood"));
        armor.put(ItemEffect.CHESTPLATE, ItemRegistry.getItem("Traveler’s Leather"));
        armor.put(ItemEffect.BOOTS, ItemRegistry.getItem("Traveler’s Boots"));

        player = new Player("TestHero", PlayerTypes.Warrior,
                1, 0, 0, 0, 0, "Spawn", armor,
                ItemRegistry.getItem("Bronze Sword"), new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null,  // mission
                null);
        Game.setPlayer(player);
        statsCommand = new StatsCommand();
    }

    @Test
    void testStatsPrintsCharacterInfo() {
        statsCommand.execute(new String[]{"stats"});
        assertTrue(output.toString().contains("Player current stats"));
    }

    @Test
    void testStatsWithInvalidArgs() {
        statsCommand.execute(new String[]{"stats", "extra"});
        assertTrue(output.toString().contains("Usage"));
    }

    @Test
    void testStatsWhenChanged() {
        //armor change
        statsCommand.execute(new String[]{"stats"});
        String out = output.toString();
        assertTrue(out.contains("Armor       : " + player.getPlayerCharacter().getArmor()));

    }
    @Test
    void testStatsWhenNoArmor() {
        //armor change
        player.setArmor(null);
        player.recalculateStats();
        statsCommand.execute(new String[]{"stats"});
        String out = output.toString();
        assertEquals(0,player.getPlayerCharacter().getArmor());
        assertTrue(out.contains("Armor       : " + player.getPlayerCharacter().getArmor()));

    }

    @Test
    void testStatsWithWeapon() {
        //armor change
        statsCommand.execute(new String[]{"stats"});
        String out = output.toString().replaceAll("\\s+", "");
        assertEquals(12,player.getPlayerCharacter().getAttack());
        assertTrue(out.contains("Attack:" + player.getPlayerCharacter().getAttack()));

    }
    @Test
    void testStatsWithNoWeapon() {
        //armor change
        player.setWeapon(null);
        player.recalculateStats();
        statsCommand.execute(new String[]{"stats"});
        String out = output.toString().replaceAll("\\s+", "");
        assertEquals(6,player.getPlayerCharacter().getAttack());
        assertTrue(out.contains("Attack:" + player.getPlayerCharacter().getAttack()));

    }
}
