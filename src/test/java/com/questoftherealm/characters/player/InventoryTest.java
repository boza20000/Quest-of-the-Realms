package com.questoftherealm.characters.player;

import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryTest {

    private Inventory inventory;
    private GameState gameState;
    private Output output;
    private Item stackableItem;
    private Item nonStackableItem;
    private LocalizationService mockMessages;
    private ItemRegistry mockItemRegistry;
    private MessageBundle mockBundle;

    @BeforeEach
    void setup() {
        inventory = new Inventory(5);


        mockMessages = mock(LocalizationService.class);
        mockItemRegistry = mock(ItemRegistry.class);
        mockBundle = mock(MessageBundle.class);

        when(mockMessages.getBundle()).thenReturn(mockBundle);
        when(mockBundle.get(anyString(), any())).thenReturn("TEST MESSAGE");
        when(mockBundle.get(anyString())).thenReturn("TEST MESSAGE");


        GameServices services = mock(GameServices.class);
        output = mock(Output.class);
        when(services.getOutput()).thenReturn(output);

        gameState = mock(GameState.class);
        when(gameState.getGameServices()).thenReturn(services);
        when(gameState.getMessages()).thenReturn(mockMessages);
        when(gameState.getItemRegistry()).thenReturn(mockItemRegistry);

        stackableItem = mock(Item.class);
        when(stackableItem.isStackable()).thenReturn(true);
        when(stackableItem.getName()).thenReturn("Potion");

        nonStackableItem = mock(Item.class);
        when(nonStackableItem.isStackable()).thenReturn(false);
        when(nonStackableItem.getName()).thenReturn("Sword");
    }

    @Test
    void givenStackableItemWithinLimit_whenAddItem_thenQuantityIsUpdated() {
        inventory.addItem(stackableItem, 3, gameState);

        assertEquals(3, inventory.getQuantity(stackableItem));
        verify(output).println(nullable(String.class));
    }

    @Test
    void givenStackableItemExceedingLimit_whenAddItem_thenQuantityIsCapped() {

        inventory.addItem(stackableItem, GameConstants.MAX_ITEMS_IN_STACK, gameState);
        inventory.addItem(stackableItem, 1, gameState);
        assertTrue(inventory.getQuantity(stackableItem) <= GameConstants.MAX_ITEMS_IN_STACK);
        verify(output, atLeastOnce()).println(nullable(String.class));
    }

    @Test
    void givenNonStackableItemWithCapacity_whenAddItem_thenItemIsAdded() {
        inventory.addItem(nonStackableItem, 1, gameState);
        assertEquals(1, inventory.getQuantity(nonStackableItem));
        verify(output).println(nullable(String.class));
    }

    @Test
    void givenNonStackableItemWhenInventoryFull_whenAddItem_thenItemIsRejected() {
        for (int i = 0; i < 5; i++) {
            Item item = mock(Item.class);
            when(item.isStackable()).thenReturn(false);
            when(item.getName()).thenReturn("Item" + i);
            inventory.addItem(item, 1, gameState);
        }

        Item newItem = mock(Item.class);
        when(newItem.isStackable()).thenReturn(false);
        when(newItem.getName()).thenReturn("UniqueItem");
        inventory.addItem(newItem, 1, gameState);
        assertFalse(inventory.containsItem(newItem));
        verify(output, atLeastOnce()).println(nullable(String.class));
    }

    @Test
    void givenStackableItemWithQuantity_whenRemovePartially_thenQuantityDecreases() {
        inventory.addItem(stackableItem, 5, gameState);
        inventory.removeItem(stackableItem, 2, gameState);
        assertEquals(3, inventory.getQuantity(stackableItem));
        verify(output, atLeastOnce()).println(nullable(String.class));
    }

    @Test
    void givenStackableItemQuantityEqualRemoval_whenRemove_thenItemIsRemoved() {
        inventory.addItem(stackableItem, 3, gameState);
        inventory.removeItem(stackableItem, 3, gameState);
        assertFalse(inventory.containsItem(stackableItem));
        verify(output, atLeastOnce()).println(nullable(String.class));
    }

    @Test
    void givenMissingItem_whenRemoveItem_thenInventoryStaysUnchanged() {
        inventory.removeItem(stackableItem, 1, gameState);
        assertEquals(0, inventory.getQuantity(stackableItem));
        verify(output).println(nullable(String.class));
    }

    @Test
    void givenEmptyInventory_whenListItems_thenPrintsEmptyInfo() {
        inventory.listItems(gameState.getGameServices().getOutput(), gameState.getMessages().getBundle());
        verify(output, atLeast(1)).println(nullable(String.class));
    }

    @Test
    void givenInventoryWithItems_whenListItems_thenPrintsItemLines() {
        inventory.addItem(stackableItem, 5, gameState);
        inventory.listItems(gameState.getGameServices().getOutput(), gameState.getMessages().getBundle());
        verify(output, atLeast(2)).println(nullable(String.class));
    }


    @Test
    void givenInventoryWithItems_whenClear_thenInventoryBecomesEmpty() {
        inventory.addItem(stackableItem, 5, gameState);
        inventory.clear();
        assertTrue(inventory.getItems().isEmpty());
    }


    @Test
    void givenInventorySnapshot_whenGetItems_thenReturnsDefensiveCopy() {
        inventory.addItem(stackableItem, 5, gameState);
        Map<Item, Integer> copy = inventory.getItems();
        copy.clear();
        assertEquals(5, inventory.getQuantity(stackableItem));
    }
}