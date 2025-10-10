package com.questoftherealm.expeditions.missions;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.expeditions.QuestFactory;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.map.Map;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;
import org.junit.jupiter.api.BeforeEach;
import java.util.Queue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    }

}
