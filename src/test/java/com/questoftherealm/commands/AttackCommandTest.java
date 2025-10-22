package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyFactory;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.enemyEntities.entities.Goblin;
import com.questoftherealm.expeditions.Mission;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class AttackCommandTest {
    private Player player;
    private Tile currentTile;
    private Command attackCommand;
    private Map gameMap;

    @BeforeEach
    void setup() throws Exception {
        gameMap = Map.getInstance();
        Tile[][] tiles = new Tile[8][8];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                tiles[y][x] = new Tile(TileTypes.GRASS, "Zone " + x + "," + y, true);
            }
        }

        // Inject tile grid into Map
        var field = Map.class.getDeclaredField("gameMap");
        field.setAccessible(true);
        field.set(gameMap, tiles);

        // Also inject map into Game
        var gmField = Game.class.getDeclaredField("gameMap");
        gmField.setAccessible(true);
        gmField.set(null, gameMap);

        // 🧍 Create player
        player = new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,       // Level, XP, gold
                0, 0,          // Position X,Y (we’ll use 0,0)
                "Spawn",
                null, null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, null
        );
        Game.setPlayer(player);

        // 🧌 Create and place an enemy on the same tile as player
        currentTile = tiles[0][0];
        Enemy goblin = EnemyFactory.createEnemy(EnemyType.GOBLIN);
        currentTile.getEnemies().add(goblin);

        // 🗡️ Load command
        attackCommand = new CommandFactory().getCommand("attack");

    }

    @Test
    void testAttackSimulatedBattle() {
        String simulatedInput = "1\n1\n1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        String[] args = {"attack", "Goblin"};
        attackCommand.execute(args);
        boolean stillExists = currentTile.getEnemies().stream()
                .anyMatch(e -> e.getType().equals(EnemyType.GOBLIN));
        assertFalse(stillExists, "Goblin should be defeated and removed from the tile");
    }

    @Test
    void testAttackUnknownEnemyDoesNothing() {
        String simulatedInput = "1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        String[] args = {"attack", "Wolf"};
        attackCommand.execute(args);

        assertEquals(1, currentTile.getEnemies().size(), "Tile should still have the goblin");
    }

    @Test
    void testAttackMissingArgument() {
        String[] args = {"attack"};
        attackCommand.execute(args);
        assertEquals(1, currentTile.getEnemies().size());
    }

}
