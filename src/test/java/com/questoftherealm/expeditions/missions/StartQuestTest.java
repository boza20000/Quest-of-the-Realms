package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field;
import java.util.Queue;
import static org.junit.jupiter.api.Assertions.*;

class StartQuestTest {

    private Player player;
    private StartQuest startQuest;
    private Queue<Quest> quests;
    private Map gameMap;

    @BeforeEach
    void setup() throws Exception {
        // Create singleton Map instance
        gameMap = Map.getInstance();
        Tile[][] dummyTiles = new Tile[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                dummyTiles[y][x] = new Tile(TileTypes.GRASS, "Zone " + x + "," + y, true);
            }
        }
        // Inject dummyTiles via reflection
        var field = Map.class.getDeclaredField("gameMap");
        field.setAccessible(true);
        field.set(gameMap, dummyTiles);

        // Set Map in Game singleton
        var gameMapField = Game.class.getDeclaredField("gameMap");
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

        setElderHasTalked(false);

        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);

        for (Quest q : quests) {
            if (q instanceof StartQuest) {
                startQuest = (StartQuest) q;
                break;
            }
        }

        // Make sure the player is synchronized with this quest (important!)
        player.setCurQuest(startQuest);
        player.setCurMission(startQuest.getMissions().get(0));

        // Ensure empty inventory to begin with
        player.getInventory().getItems().clear();

        // Safe initial move so triggers/positions are consistent
        player.move(player.getX(), player.getY());
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear QuestFactory queue to avoid cross-test pollution
        QuestFactory.getQuests().clear();

        // Reset game player
        Game.setPlayer(null);

        // Reset Elder flag
        setElderHasTalked(false);

        // Reset player's inventory and other static mission flags that might be used elsewhere
        player = null;
    }

    // Utility to flip the private static Elder.hasTalked via reflection
    private static void setElderHasTalked(boolean value) throws Exception {
        Field f = Elder.class.getDeclaredField("hasTalked");
        f.setAccessible(true);
        f.set(null, value);
    }

    @Test
    @DisplayName("Meet_the_Elder completes only when Elder.hasTalked == true and player is on StartQuest")
    void meetTheElder_onlyCompletesWithElderTalkedAndCorrectQuest() throws Exception {
        var mission = (Meet_the_Elder) startQuest.getMissions().get(0);

        // ensure initial state
        setElderHasTalked(false);
        mission.setCompleted(false);

        // not talked yet -> should not complete
        assertFalse(mission.checkCompletion(), "Mission should not complete before the elder has talked");
        assertFalse(mission.isCompleted());

        // simulate elder talk
        setElderHasTalked(true);

        // now mission should complete
        assertTrue(mission.checkCompletion(), "Mission should complete after elder has talked");
        assertTrue(mission.isCompleted());

        // idempotence: additional calls stay true and do not change state
        assertTrue(mission.checkCompletion(), "Repeated checkCompletion should remain true");
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Meet_the_Elder does NOT complete if player's curQuest is not StartQuest")
    void meetTheElder_doesNotCompleteIfNotOnStartQuest() throws Exception {
        var mission = (Meet_the_Elder) startQuest.getMissions().get(0);

        // make sure elder has talked
        setElderHasTalked(true);
        mission.setCompleted(false);

        // temporarily set player to a different quest (NorthExploration) -> should prevent completion
        player.setCurQuest(new NorthExploration());
        player.setCurMission(null);

        assertFalse(mission.checkCompletion(), "Mission should not complete when player is on a different quest");

        // restore StartQuest on player
        player.setCurQuest(startQuest);
        player.setCurMission(mission);

        // Now it should complete
        assertTrue(mission.checkCompletion(), "Mission should complete when player returns to StartQuest and elder is talked to");
    }

    @Test
    @DisplayName("Gather_Supplies completes only when at least 1 potion AND >=5 food are present (edge cases tested)")
    void gatherSupplies_edgeCasesAndIdempotence() {
        var mission = (Gather_Supplies) startQuest.getMissions().get(1);

        // ensure clean slate
        player.getInventory().clear();
        mission.setCompleted(false);

        // 1) empty inventory -> fail
        assertFalse(mission.checkCompletion(), "Empty inventory should not satisfy the mission");

        // 2) only potion -> fail (not enough food)
        player.getInventory().addItem(ItemRegistry.getItem("Health Potion"), 1);
        assertFalse(mission.checkCompletion(), "Having a potion but insufficient food should not complete mission");

        // 3) only food but <5 -> fail
        player.getInventory().clear();
        player.getInventory().addItem(ItemRegistry.getItem("Dried Meat"), 4);
        assertFalse(mission.checkCompletion(), "Having <5 food but no potion should not complete mission");

        // 4) have food >=5 but no potion -> fail
        player.getInventory().clear();
        player.getInventory().addItem(ItemRegistry.getItem("Dried Meat"), 5);
        assertFalse(mission.checkCompletion(), "Having enough food but missing potion should not complete mission");

        // 5) finally satisfy both conditions -> success
        player.getInventory().addItem(ItemRegistry.getItem("Health Potion"), 1);
        assertTrue(mission.checkCompletion(), "Mission should complete when both potion and 5 food are present");
        assertTrue(mission.isCompleted());

        // 6) idempotence: subsequent calls remain true
        assertTrue(mission.checkCompletion(), "Repeated checkCompletion should stay true after completion");
    }

    @Test
    @DisplayName("Quest completes when all missions marked completed and updateStatus is called")
    void startQuest_completesWhenAllMissionsDone() {
        // mark every mission as done
        startQuest.getMissions().forEach(m -> m.setCompleted(true));

        // update status — the quest should become completed
        startQuest.updateStatus();

        assertTrue(startQuest.isCompleted(), "StartQuest should be marked completed when all missions are done");
    }
}
