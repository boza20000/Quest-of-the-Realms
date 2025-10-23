package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.expeditions.quests.*;
import com.questoftherealm.friendlyEntities.Entities.Elder;
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
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class QuestProgressTest {
    private Player player;
    private Command questProgressCommand;
    private Map gameMap;
    private Queue<Quest> quests;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setup() throws Exception {
        // Capture console output
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        // Setup mock map
        gameMap = Map.getInstance();
        Tile[][] tiles = new Tile[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                tiles[y][x] = new Tile(TileTypes.GRASS, "Zone " + x + "," + y, true);
            }
        }
        var field = Map.class.getDeclaredField("gameMap");
        field.setAccessible(true);
        field.set(gameMap, tiles);

        var gmField = Game.class.getDeclaredField("gameMap");
        gmField.setAccessible(true);
        gmField.set(null, gameMap);

        // Setup player
        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,
                0, 0,
                "Spawn",
                null, null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null
        );
        Game.setPlayer(player);

        quests = null;
        QuestFactory.getQuests().clear();
        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);

        questProgressCommand = new CommandFactory().getCommand("progress");
        Field elderFlag = Elder.class.getDeclaredField("hasTalked");
        elderFlag.setAccessible(true);
        elderFlag.set(null, false);

        // Reset Ambushed
        Ambushed.playerAmbushed = false;

        // Reset Escape_to_Safety
        Escape_to_Safety.playerEscapedAmbush = false;

        // Reset Explore_the_Village
        Explore_the_Village.setSearched_1(false);
        Explore_the_Village.setSearched_2(false);
    }

    @Test
    void testMeetTheElderCompletion() throws Exception {
        player.setCurQuest(new StartQuest());
        player.setCurMission(new Meet_the_Elder());
        Field f = Elder.class.getDeclaredField("hasTalked");
        f.setAccessible(true);
        f.set(null, true);

        player.updateQuestStatus();
        questProgressCommand.execute(new String[]{"progress"});
        String out = output.toString();

        assertTrue(out.contains("✔"), "Meet_the_Elder should show completed");
        assertTrue(player.getCurQuest().getMissions().get(0).isCompleted());
        f.set(null, false);
        Ambushed.playerAmbushed = false;
    }

    @Test
    void testGatherSuppliesCompletion() {
        player.setCurQuest(new StartQuest());
        player.setCurMission(new Gather_Supplies());

        // Add required items
        Item food = ItemRegistry.getItem("Trail Mix");
        Item potion = ItemRegistry.getItem("Health Potion");
        player.getInventory().addItem(potion, 1);
        player.getInventory().addItem(food, 5);

        player.updateQuestStatus();
        questProgressCommand.execute(new String[]{"progress"});
        String out = output.toString();

        assertTrue(out.contains("✔"), "Gather_Supplies should be completed ✔");
        assertTrue(player.getCurQuest().getMissions().get(1).isCompleted());
    }

    @Test
    void testGoblinAmbushPartialProgress() {
        quests.poll();
        quests.poll();
        player.setCurQuest(new GoblinAmbush());
        player.setCurMission(new Explore_Nearby_Forests());
        player.getCurQuest().getMissions().get(0).setCompleted(true);
        player.getCurQuest().getMissions().get(1).setCompleted(true);

        Ambushed.playerAmbushed = true;
        player.updateQuestStatus();

        questProgressCommand.execute(new String[]{"progress"});
        String out = output.toString();

        assertTrue(out.contains("Ambushed"), "Should list Ambushed mission");
        assertTrue(out.contains("✔"), "Ambushed should now be completed ✔");
        assertFalse(out.contains("Escape to Safety ✔"), "Escape should remain incomplete ❌");
        Ambushed.playerAmbushed = false;
    }

    @Test
    void testQuestAdvancesAfterAllMissionsComplete() throws Exception {
        player.setCurQuest(new StartQuest());
        player.setCurMission(new Gather_Supplies());

        Field elderFlag = Elder.class.getDeclaredField("hasTalked");
        elderFlag.setAccessible(true);
        elderFlag.set(null, true);

        Item food = ItemRegistry.getItem("Trail Mix");
        Item potion = ItemRegistry.getItem("Health Potion");
        player.getInventory().addItem(potion, 1);
        player.getInventory().addItem(food, 5);

        //player.updateQuestStatus();
        questProgressCommand.execute(new String[]{"progress"});

        String out = output.toString();
        Quest curQuest = player.getCurQuest();

        assertTrue(curQuest.getMissions().get(0).checkCompletion(), "Meet_the_Elder should be completed by flag");
        assertTrue(curQuest.getMissions().get(1).checkCompletion(), "Gather_Supplies should be completed by inventory");
        assertTrue(curQuest.getMissions().stream().allMatch(Mission::isCompleted));

        player.updateQuestStatus();
        Quest next = quests.peek();
        assertNotNull(next, "Next quest should exist after completing StartQuest");
        assertTrue(player.getCurMission().getName().contains("North"));
    }

    @Test
    void testNoQuestActive() {
        player.setCurQuest(null);
        player.setCurMission(null);

        questProgressCommand.execute(new String[]{"progress"});
        String out = output.toString();

        assertTrue(out.contains("Error: No quest loaded"), "Should print error for missing quest");
    }

    @Test
    void allDone() {
        quests.poll();
        quests.poll();
        quests.poll();
        quests.poll();
        player.updateQuestStatus();

        var mission = new March_Into_the_Far_North();
        player.setCurMission(mission);
        player.move(GameConstants.FarNorthMountain.x(), GameConstants.FarNorthMountain.y());
        player.updateQuestStatus();
        Breach_the_Stronghold.isBreached = true;
        player.updateQuestStatus();
        Defeat_the_Goblin_King.isDefeated = true;
        player.updateQuestStatus();
        assertNull(player.getCurQuest());
        assertNull(player.getCurMission());
        questProgressCommand.execute(new String[]{"progress"});
        String out = output.toString();
        assertTrue(out.contains("No quest loaded"), "Should print error for missing quest");
    }


}
