package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("North Exploration Quest — Integration & Mission Logic Tests")
class NorthExplorationTest {
    private Player player;
    private NorthExploration quest;
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

        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,
                GameConstants.PLAYER_START.x(),
                GameConstants.PLAYER_START.y(),
                "Spawn",
                null,
                null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null,
                null,  // mission
                null
        );
        Game.setPlayer(player);

        var f1 = Villager.class.getDeclaredField("hasTalkedVillage1");
        var f2 = Villager.class.getDeclaredField("hasTalkedVillage2");
        f1.setAccessible(true);
        f2.setAccessible(true);
        f1.set(null, false);
        f2.set(null, false);
        quests = null;
        QuestFactory.getQuests().clear();
        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);
        quests.poll();//remove StartQuest
        quest = null;
        for (Quest q : quests) {
            if (q instanceof NorthExploration) {
                quest = (NorthExploration) q;
                break;
            }
        }

        assertNotNull(quest, "NorthExploration must be present in QuestFactory");
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().getFirst());
        player.getInventory().clear();
        player.move(player.getX(), player.getY());
    }

    @Test
    @DisplayName("Travel_North completes only when player Y <= North_Y")
    void travelNorthMissionCompletesProperly() {
        var mission = player.getCurMission();
        player.move(GameConstants.PLAYER_START.x(), GameConstants.North_Y + 1);
        mission.checkCompletion();
        assertFalse(mission.isCompleted(), "Should not complete before reaching the north");
        player.move(player.getX(), GameConstants.North_Y - 1);
        mission.checkCompletion();
        assertTrue(mission.isCompleted(), "Mission should complete when player reaches north border");
        assertTrue(mission.isCompleted(), "Mission flag should persist after completion");
        assertTrue(mission.checkCompletion(), "Repeated check should remain true after completion");
    }

    @Test
    @DisplayName("Explore_the_Village completes after visiting and searching both villages")
    void exploreVillagesMissionFullCycle() {
        var mission = quest.getMissions().get(1);

        Explore_the_Village.setSearched_1(true);
        player.move(GameConstants.NorthVillage_1.x(), GameConstants.NorthVillage_1.y());
        assertFalse(mission.checkCompletion(), "Should not complete with only one village visited");

        Explore_the_Village.setSearched_2(true);
        player.move(GameConstants.NorthVillage_2.x(), GameConstants.NorthVillage_2.y());
        assertTrue(mission.checkCompletion(), "Should complete after both villages searched and visited");

        assertTrue(mission.checkCompletion(), "Re-check should remain true once completed");
    }

    @Test
    @DisplayName("Talk_To_Survivors completes only when all survivors talked to")
    void talkToSurvivorsMissionCompletesProperly() throws Exception {
        var mission = quest.getMissions().get(2);

        var f1 = Villager.class.getDeclaredField("hasTalkedVillage1");
        var f2 = Villager.class.getDeclaredField("hasTalkedVillage2");
        f1.setAccessible(true);
        f2.setAccessible(true);

        f1.set(null, true);
        f2.set(null, false);
        assertFalse(mission.checkCompletion(),
                "Mission should not complete until all survivors are talked to");

        f1.set(null, true);
        f2.set(null, true);
        assertTrue(mission.checkCompletion(),
                "Mission should complete once both survivors are talked to");
    }

    @Test
    @DisplayName("Quest completes only after all missions are done")
    void fullQuestCompletionSequence() {
        quest.getMissions().forEach(m -> m.setCompleted(true));
        quest.updateStatus();
        assertTrue(quest.isCompleted(), "Quest should be marked as completed when all missions are done");
    }

    @Test
    @DisplayName("Quest missions remain incomplete if one fails")
    void questNotCompletedIfOneMissionIncomplete() {
        quest.getMissions().forEach(m -> m.setCompleted(true));
        quest.getMissions().getLast().setCompleted(false);
        quest.updateStatus();
        assertFalse(quest.isCompleted(), "Quest should not complete if any mission is still incomplete");
    }

}
