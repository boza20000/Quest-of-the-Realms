package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartQuestTest {

    private Player player;
    private StartQuest quest;
    private Queue<Quest> quests;
    private Map gameMap;

    @BeforeEach
    void setup() throws Exception {
        // Create singleton Map instance with a dummy map
        gameMap = Map.getInstance();
        Tile[][] dummyTiles = new Tile[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                dummyTiles[y][x] = new Tile(TileTypes.GRASS, "Zone " + x + "," + y, true);
            }
        }
        // Set specific villages so missions can detect them
        dummyTiles[GameConstants.NorthVillage_1_Y][GameConstants.NorthVillage_1_X] =
                new Tile(TileTypes.VILLAGE, "Village 1", true);
        dummyTiles[GameConstants.NorthVillage_2_Y][GameConstants.NorthVillage_2_X] =
                new Tile(TileTypes.VILLAGE, "Village 2", true);

        // Inject dummyTiles via reflection
        var field = Map.class.getDeclaredField("gameMap");
        field.setAccessible(true);
        field.set(gameMap, dummyTiles);

        // **Set Map in Game singleton**
        var gameMapField = Game.class.getDeclaredField("gameMap");
        gameMapField.setAccessible(true);
        gameMapField.set(null, gameMap);

        // Create Player
        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,
                GameConstants.PLAYER_START_X,
                GameConstants.PLAYER_START_Y,
                "Spawn",
                null, null, null
        );
        Game.setPlayer(player);

        // Reset static flags for villages
        Explore_the_Village.setSearched_1(false);
        Explore_the_Village.setSearched_2(false);
        var f1 = Villager.class.getDeclaredField("hasTalkedVillage1");
        f1.setAccessible(true);
        f1.set(null, false);
        var f2 = Villager.class.getDeclaredField("hasTalkedVillage2");
        f2.setAccessible(true);
        f2.set(null, false);

        // Create quest
        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);
        for (Quest q : quests) {
            if (q instanceof StartQuest) {
                quest = (StartQuest) q;
                break;
            }
        }
        // Safe initial move
        player.move(player.getX(), player.getY());
    }

    @Test
    void testTalkWithElder() throws Exception {
        var mission = quest.getMissions().get(0);
        var f1 = Elder.class.getDeclaredField("hasTalked");
        f1.setAccessible(true);
        f1.set(null,false);
        mission.checkCompletion();
        player.move(GameConstants.CastleX,GameConstants.CastleY);
        assertFalse(Elder.isHasTalked(), "Elder should be talking for the first time");
        assertTrue(mission.checkCompletion(),"Player is in the castle should be ready");

    }

    @Test
    void test_GatherSupplies() throws Exception {
        var mission = quest.getMissions().get(1);
        assertFalse(mission.checkCompletion(),"Shouldn't be completed yet");
        player.getInventory().addItem(ItemRegistry.getItem("Health Potion"),1);
        player.getInventory().addItem(ItemRegistry.getItem("Health Potion"),1);
        player.getInventory().addItem(ItemRegistry.getItem("Dried Meat"),5);
        assertTrue(mission.checkCompletion(),"Should be completed");
    }

    @Test
    void test_StartQuest() {
        quest.getMissions().forEach( m -> m.setCompleted(true));
        quest.updateStatus();
        assertTrue(quest.isCompleted(),"Start is completed");
    }
}
