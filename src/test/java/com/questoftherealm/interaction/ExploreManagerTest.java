package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.map.Locations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

class ExploreManagerTest {

    private GameState state;
    private GameServices services;
    private Input input;
    private Output output;
    private RandomService random;
    private Player player;
    private SlowPrinter slowPrinter;
    private Chest chest;
    private Enemy enemy;
    private Inventory inventory;
    private ExploreManager exploreManager;

    @BeforeEach
    void setUp() {
        // Mocks
        input = mock(Input.class);
        output = mock(Output.class);
        random = mock(RandomService.class);
        services = mock(GameServices.class);
        state = mock(GameState.class);
        player = mock(Player.class);
        slowPrinter = mock(SlowPrinter.class);
        chest = mock(Chest.class);
        enemy = mock(Enemy.class);
        inventory = mock(Inventory.class);
        
        // Use real LocalizationService to get actual messages
        LocalizationService localizationService = new LocalizationService();

        // Wiring
        when(state.getGameServices()).thenReturn(services);
        when(services.getInput()).thenReturn(input);
        when(services.getOutput()).thenReturn(output);
        when(services.getRandom()).thenReturn(random);
        when(state.getMessages()).thenReturn(localizationService);
        when(player.getInventory()).thenReturn(inventory);

        // System Under Test with overridden factories to inject mocks
        exploreManager = new ExploreManager() {
            @Override
            protected SlowPrinter createSlowPrinter(GameState state) {
                return slowPrinter;
            }

            @Override
            protected Chest createChest(GameState state) {
                return chest;
            }

            @Override
            protected Enemy createEnemy(EnemyType type, GameState state) {
                return enemy;
            }

            @Override
            protected void pause() {
                // No-op to make tests fast
            }
        };
    }

    @Test
    void exploreTower_WhenGhostEncountered_ThenInteractsWithMockedEnemy() {
        // Arrange
        when(input.nextLine()).thenReturn("1"); // Enter structure
        when(random.randomInt(100)).thenReturn(20); // Outcome < 30 triggers Ghost

        // Act
        exploreManager.exploreStructure(Locations.ABANDONED_TOWER, player, state);

        // Assert
        // Check for specific text appearing in the output via SlowPrinter
        // This confirms the correct branch was taken
        verify(slowPrinter).slowPrint(contains("restless spirit"));
        
        // Verify interaction happened on the MOCKED enemy.
        // Since 'enemy' is a mock, the real interact() method (which loops) is NOT called.
        verify(enemy).interact(player, state); 
    }

    @Test
    void exploreTower_WhenChestFound_ThenAddsItemToInventory() {
        // Arrange
        when(input.nextLine()).thenReturn("1"); // Enter structure
        when(random.randomInt(100)).thenReturn(40); // Outcome 30-60 triggers Chest

        Item mockItem = mock(Item.class);
        when(mockItem.getName()).thenReturn("Gold");
        ItemDrop drop = new ItemDrop(mockItem, 10);
        when(chest.generateRandomItem()).thenReturn(drop);

        // Act
        exploreManager.exploreStructure(Locations.ABANDONED_TOWER, player, state);

        // Assert
        verify(slowPrinter).slowPrint(contains("old wooden chest"));
        verify(chest).generateRandomItem();
        verify(inventory).addItem(mockItem, 10, state);
    }

    @Test
    void exploreTower_WhenObservingAndSpotted_ThenPrintsSpottedMessage() {
        // Arrange
        when(input.nextLine()).thenReturn("2"); // Observe
        when(random.randomInt(100)).thenReturn(10); // Outcome < 30 triggers Spotted

        // Act
        exploreManager.exploreStructure(Locations.ABANDONED_TOWER, player, state);

        // Assert
        verify(slowPrinter).slowPrint(contains("sudden movement"));
    }

    @Test
    void exploreTower_WhenLeaving_ThenPrintsLeaveMessage() {
        // Arrange
        when(input.nextLine()).thenReturn("3"); // Leave

        // Act
        exploreManager.exploreStructure(Locations.ABANDONED_TOWER, player, state);

        // Assert
        verify(slowPrinter).slowPrint(contains("decide not to push your luck"));
    }
}
