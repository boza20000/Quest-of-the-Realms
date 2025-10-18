package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.GoblinAmbush;
import com.questoftherealm.expeditions.quests.NorthExploration;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Queue;
import static org.junit.jupiter.api.Assertions.*;

public class GoblinAmbushTest {
    private Player player;
    private GoblinAmbush quest;
    private Queue<Quest> quests;
    private Map gameMap;

    @BeforeEach
    void setup() throws Exception {
        // Create singleton Map instance with dummy data
        gameMap = Map.getInstance();
        Tile[][] dummyTiles = new Tile[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                dummyTiles[y][x] = new Tile(TileTypes.GRASS, "Zone " + x + "," + y, true);
            }
        }

        // Add Goblin Camp
        dummyTiles[GameConstants.Goblin_Camp.x()][GameConstants.Goblin_Camp.y()] =
                new Tile(TileTypes.FOREST, "Secret Goblin Camp", true);

        // Inject into Map singleton
        var field = Map.class.getDeclaredField("gameMap");
        field.setAccessible(true);
        field.set(gameMap, dummyTiles);

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

        quests = null;
        QuestFactory.getQuests().clear();
        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);
        quests.poll();//remove StartQuest
        quests.poll();//remove NorthExploration
        quest = null;
        for (Quest q : quests) {
            if (q instanceof GoblinAmbush) {
                quest = (GoblinAmbush) q;
                break;
            }
        }

        assertNotNull(quest, "GoblinAmbush must be present in QuestFactory");
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().getFirst());
        player.getInventory().clear();
        player.move(player.getX(), player.getY());
    }

    @Test
    void testExploreTheNearbyForestsTest() {
        var mission = quest.getMissions().get(0);
        player.move(GameConstants.Goblin_Camp.x()+1, GameConstants.Goblin_Camp.y());
        mission.checkCompletion();
        assertFalse(mission.isCompleted(),"Wrong position");
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y()+1);
        mission.checkCompletion();
        assertFalse(mission.isCompleted(),"Wrong position");
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y());
        mission.checkCompletion();
        assertTrue(mission.isCompleted(), "Camp found");
    }

    @Test
    void testInfiltrateCampTest() {
        var mission = quest.getMissions().get(1);
        mission.checkCompletion();
        assertFalse(mission.checkCompletion(), "Not there");
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y());
        Explore_Nearby_Forests.campFound = false;
        assertFalse(mission.checkCompletion(), "Camp found but not interacted");
        Explore_Nearby_Forests.campFound = true;
        assertTrue(mission.checkCompletion(),"Trigger is true");
    }

    @Test
    void testAmbushed() {
        var mission = quest.getMissions().get(2);
        var mission1 = quest.getMissions().get(1);
        var mission0 = quest.getMissions().get(0);
        mission0.setCompleted(true);
        mission1.setCompleted(true);
        // Should not trigger until previous missions done
        assertFalse(mission.checkCompletion(), "Ambush not triggered yet");
        Explore_Nearby_Forests.campFound = true;
        // Simulate entering the goblin camp — this should trigger the ambush
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y());
        mission.checkCompletion();

        assertFalse(mission.isCompleted(), "After you investigated the camp you were spotted");
        Ambushed.playerAmbushed = true;
        mission.checkCompletion();
        assertTrue(mission.isCompleted(),"Ambush triggered successfully");
    }

    @Test
    void testEscapeToSafety() {
        var mission = quest.getMissions().get(3);
        assertFalse(mission.isCompleted(), "camp not found");
        Explore_Nearby_Forests.campFound = true;
        assertFalse(mission.isCompleted(), "didn't escape");
        Escape_to_Safety.playerEscapedAmbush = true;
        assertFalse(mission.isCompleted(), "Not in the area");
        player.move(GameConstants.Goblin_Camp.x(), GameConstants.Goblin_Camp.y());
        mission.checkCompletion();
        assertTrue(mission.isCompleted(), "Successfully escaped the ambush area");
    }
}
