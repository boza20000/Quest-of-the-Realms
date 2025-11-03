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
import com.questoftherealm.friendlyEntities.Npc;
import com.questoftherealm.friendlyEntities.NpcType;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.*;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class StartQuestTest {

    private Player player;
    private StartQuest startQuest;
    private Queue<Quest> quests;
    private Map gameMap;
    private Elder elder;

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
                GameConstants.Castle.x(),
                GameConstants.Castle.y(),
                "Castle",
                null, // armor
                null, // weapon
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, // quest
                null,  // mission
                null
        );
        Game.setPlayer(player);

        // Initialize quests
        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);

        for (Quest q : quests) {
            if (q instanceof StartQuest) {
                startQuest = (StartQuest) q;
                break;
            }
        }

        player.setCurQuest(startQuest);
        player.setCurMission(startQuest.getMissions().get(0));

        player.getInventory().getItems().clear();

        Tile startTile = gameMap.curZone(GameConstants.Castle.x(), GameConstants.Castle.y());
        elder = new Elder();
        startTile.registerNpc(elder);

        player.move(player.getX(), player.getY());
    }

    @AfterEach
    void tearDown() throws Exception {
        QuestFactory.getQuests().clear();
        Game.setPlayer(null);
        player = null;
        elder = null;
    }

    @Test
    @DisplayName("Meet_the_Elder completes only when Elder.hasTalked == true and player is on StartQuest")
    void meetTheElder_onlyCompletesWithElderTalkedAndCorrectQuest() {
        var mission = (Meet_the_Elder) startQuest.getMissions().get(0);

        // ensure Elder is on this tile
        Tile curTile = gameMap.curZone(player.getX(), player.getY());
        Npc npc = curTile.getNpcByType(NpcType.Elder);
        assertNotNull(npc, "Elder NPC should be present on the tile");
        Elder elder = (Elder) npc;

        // ensure initial state
        assertFalse(elder.isHasTalked(), "Elder should not have been talked to yet");
        mission.setCompleted(false);

        // not talked yet -> should not complete
        assertFalse(mission.checkCompletion(), "Mission should not complete before elder has talked");
        assertFalse(mission.isCompleted());

        // simulate elder talk
        elder.talk(player,true);

        // now mission should complete
        assertTrue(mission.checkCompletion(), "Mission should complete after elder has talked");
        assertTrue(mission.isCompleted());

        // idempotence: repeated calls
        assertTrue(mission.checkCompletion());
        assertTrue(mission.isCompleted());
    }

    @Test
    @DisplayName("Meet_the_Elder does NOT complete if player's curQuest is not StartQuest")
    void meetTheElder_doesNotCompleteIfNotOnStartQuest() {
        var mission = (Meet_the_Elder) startQuest.getMissions().get(0);

        // make sure elder has talked
        Tile curTile = gameMap.curZone(player.getX(), player.getY());
        Elder elder = (Elder) curTile.getNpcByType(NpcType.Elder);
        elder.talk(player,true);
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
       for(Mission m : startQuest.getMissions()){
           m.setCompleted(true);
       }
        startQuest.updateStatus();
        assertTrue(startQuest.isCompleted(), "StartQuest should be marked completed when all missions are done");
    }
}
