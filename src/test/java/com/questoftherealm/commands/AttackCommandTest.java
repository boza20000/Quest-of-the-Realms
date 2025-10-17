package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.missions.Explore_the_Village;
import com.questoftherealm.expeditions.quests.StartQuest;
import com.questoftherealm.friendlyEntities.Entities.Villager;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import java.util.Queue;

public class AttackCommandTest {
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
        dummyTiles[GameConstants.NorthVillage_1.y()][GameConstants.NorthVillage_1.x()] =
                new Tile(TileTypes.VILLAGE, "Village 1", true);
        dummyTiles[GameConstants.NorthVillage_2.y()][GameConstants.NorthVillage_2.x()] =
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
                GameConstants.PLAYER_START.x(),
                GameConstants.PLAYER_START.y(),
                "Spawn",
                null, // armor
                null, // weapon
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null, // quest
                null  // mission
        );;
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






}
