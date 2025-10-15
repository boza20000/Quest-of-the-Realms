package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.interaction.Interactions;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class RiseOfTheGoblinThreatTest {
    private Player player;
    private RiseOfTheGoblinThreat quest;
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
        quests.poll();//remove GoblinAmbush
        quest = null;
        for (Quest q : quests) {
            if (q instanceof RiseOfTheGoblinThreat) {
                quest = (RiseOfTheGoblinThreat) q;
                break;
            }
        }

        assertNotNull(quest, "RiseOfTheGoblinThreat must be present in QuestFactory");
        player.setCurQuest(quest);
        player.setCurMission(quest.getMissions().getFirst());
        player.getInventory().clear();
        player.move(player.getX(), player.getY());
        for (Mission m : quest.getMissions()) {
            m.setCompleted(false);
        }
        Assemble_an_Army.knightsTriedToRecruit = false;
        Assemble_an_Army.archersTriedToRecruit = false;
        Assemble_an_Army.magesTriedToRecruit = false;
        Assemble_an_Army.knightsRecruited = false;
        Assemble_an_Army.archersRecruited = false;
        Assemble_an_Army.magesRecruited = false;
        Assemble_an_Army.reportedToKing = false;
    }

    @Test
    void testWarnTheCastle() {
        var curMission = quest.getMissions().getFirst();
        assertFalse(curMission.isCompleted(), "Didnt do the mission yet");
        player.setCurMission(curMission);
        player.move(GameConstants.Castle.x(), GameConstants.Castle.y());
        quest.getMissions().getFirst().checkCompletion();
        assertTrue(curMission.isCompleted(), "True");
        assertNotNull(player.getCurMission(), "Player should have an active mission");
        player.updateQuestStatus();
        System.out.println(player.getCurMission().getName());
        assertTrue(player.getCurMission().getName().contains("Assemble"),
                "After warning the castle, next mission should be Assemble an Army");
        assertFalse(player.getCurMission().isCompleted(), "next mission is not ready");
    }

    @Test
    void testAssembleAnArmy() {
        Assemble_an_Army.reportedToKing = true;
        var curMission = player.getCurQuest().getMissions().get(1);
        assertNotNull(curMission);

        Assemble_an_Army.knightsTriedToRecruit = false;
        Assemble_an_Army.archersTriedToRecruit = false;
        Assemble_an_Army.magesTriedToRecruit = false;

        Assemble_an_Army.knightsRecruited = false;
        Assemble_an_Army.archersRecruited = false;
        Assemble_an_Army.magesRecruited = false;

        assertFalse(curMission.isCompleted());

        Assemble_an_Army.knightsTriedToRecruit = true;
        Assemble_an_Army.archersTriedToRecruit = true;
        Assemble_an_Army.magesTriedToRecruit = true;

        curMission.checkCompletion();
        assertTrue(curMission.isCompleted(), "All warriors called");

        player.updateQuestStatus();
        assertTrue(player.getCurMission().getName().contains("Goblin General"));
        assertFalse(player.getCurMission().isCompleted());

    }

    @Test
    void testDefeatTheGoblinGeneral_success() {
        var mission = quest.getMissions().getLast();
        Assemble_an_Army.reportedToKing = true;
        Assemble_an_Army.knightsTriedToRecruit = true;
        Assemble_an_Army.archersTriedToRecruit = true;
        Assemble_an_Army.magesTriedToRecruit = true;
        Assemble_an_Army.knightsRecruited = true;
        Assemble_an_Army.archersRecruited = true;
        Assemble_an_Army.magesRecruited = true;
        Assemble_an_Army.updateArmyStatus();
        int roll = new Random().nextInt(2);
        Defeat_the_Goblin_General.isDefeated = (roll == 0);
        if (Defeat_the_Goblin_General.isDefeated) {
            mission.checkCompletion();
            assertTrue(mission.isCompleted());
        } else {
            player.getPlayerCharacter().setHealth(0);
            mission.checkCompletion();
            assertFalse(mission.isCompleted());
            assertTrue(player.getPlayerCharacter().isDead());
        }
    }

    @Test
    void testQuest() {
        player.updateQuestStatus();
        assertFalse(quest.isCompleted());
        for (Mission m : quest.getMissions()) {
            m.setCompleted(true);
        }
        player.updateQuestStatus();
        assertTrue(quest.isCompleted());
        assertNotNull(quest);
        assertTrue(player.getCurQuest().getName().contains("Final"));
    }


}
