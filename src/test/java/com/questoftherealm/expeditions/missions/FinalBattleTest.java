package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FinalBattleTest {
    private Player player;
    private FinalBattle quest;
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
                null ,  // mission
                null
        );
        Game.setPlayer(player);

        quests = null;
        QuestFactory.getQuests().clear();
        new QuestFactory();
        quests = QuestFactory.getQuests();
        Game.setGameQuests(quests);
        quests.poll();//remove StartQuest
        quests.poll();//remove NorthExploration
        quests.poll();//remove GoblinAmbush
        quests.poll();//remove RiseOfTheGoblinTreat
        quest = null;
        for (Quest q : quests) {
            if (q instanceof FinalBattle) {
                quest = (FinalBattle) q;
                break;
            }
        }

        assertNotNull(quest, "FinalBattle must be present in QuestFactory");
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().getFirst());
        player.getInventory().clear();
        player.move(player.getX(), player.getY());
        for (Mission m : quest.getMissions()) {
            m.setCompleted(false);

        }
    }

    @Test
    void testMarchIntoFarNorthCompletion() {
        // Arrange
        var mission = new March_Into_the_Far_North();
        player.setCurQuest(quest);
        player.setCurMission(mission);

        // Initially not completed
        assertFalse(mission.isCompleted(), "Mission should start incomplete");

        // Act - move player to Far North
        player.move(GameConstants.FarNorthMountain.x(), GameConstants.FarNorthMountain.y());
        mission.checkCompletion();

        // Assert
        assertTrue(mission.isCompleted(), "Mission should complete when player reaches Far North");
    }

    @Test
    void testMarchIntoFarNorthFailsIfWrongQuest() {
        // Arrange: Player is on a different quest
        player.setCurQuest(new RiseOfTheGoblinThreat());
        var mission = new March_Into_the_Far_North();
        player.setCurMission(mission);

        // Act
        player.move(GameConstants.FarNorthMountain.x(), GameConstants.FarNorthMountain.y());
        boolean completed = mission.checkCompletion();

        // Assert
        assertFalse(completed, "Mission shouldn't complete for a different quest");
        assertFalse(mission.isCompleted());
    }

    @Test
    void testBreachTheStrongholdCompletion() {
        // Arrange
        var mission = new Breach_the_Stronghold();
        Breach_the_Stronghold.isBreached = false;
        player.setCurQuest(quest);
        player.setCurMission(mission);

        // Act 1: not yet breached
        assertFalse(mission.checkCompletion(), "Should not complete yet");

        // Act 2: simulate breach
        Breach_the_Stronghold.isBreached = true;
        boolean result = mission.checkCompletion();

        // Assert
        assertTrue(result, "Should complete when stronghold is breached");
        assertTrue(mission.isCompleted());
    }

    @Test
    void testBreachTheStrongholdDoesNotCompleteIfFlagFalse() {
        var mission = new Breach_the_Stronghold();
        player.setCurQuest(quest);
        player.setCurMission(mission);

        Breach_the_Stronghold.isBreached = false;
        boolean result = mission.checkCompletion();

        assertFalse(result, "Should not complete if not breached");
        assertFalse(mission.isCompleted());
    }

    @Test
    void testDefeatGoblinKingCompletion() {
        // Arrange
        var mission = new Defeat_the_Goblin_King();
        player.setCurQuest(quest);
        player.setCurMission(mission);

        // Act
        Defeat_the_Goblin_King.isDefeated = false;
        boolean resultBefore = mission.checkCompletion();

        // Assert - still false
        assertFalse(resultBefore, "Should not complete before defeat");

        // Act - simulate victory
        Defeat_the_Goblin_King.isDefeated = true;
        boolean resultAfter = mission.checkCompletion();

        // Assert - completed
        assertTrue(resultAfter, "Should complete after goblin king is defeated");
        assertTrue(mission.isCompleted());
    }

    @Test
    void testQuestFullyCompletesWhenAllMissionsDone() {
        for (Mission m : quest.getMissions()) {
            m.setCompleted(true);
        }

        player.setCurQuest(quest);
        player.updateQuestStatus();

        assertTrue(quest.isCompleted(), "Quest should complete when all missions are done");
    }


}
