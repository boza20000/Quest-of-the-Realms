package com.questoftherealm.FriendlyEntitiesTest;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Warrior;
import com.questoftherealm.friendlyEntities.Entities.Trader;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.items.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraderTest {

    private Output output;
    private Input input;
    private GameState state;
    private Trader trader;
    private MissionInteractions interactions;

    @BeforeEach
    void setUp() {
        output = mock(Output.class);
        input = mock(Input.class);
        interactions = mock(MissionInteractions.class);

        GameServices services = new GameServices(output, input);
        
        try {
            state = new GameState("TestWorld", services);
        } catch (Exception e) {
            state = mock(GameState.class);
            when(state.getGameServices()).thenReturn(services);
        }
    }

    @Test
    void given_TraderCreated_when_Inspected_then_HasItemsForSale() throws Exception {
        trader = new Trader("TraderJoe", state, interactions);
        Map<Item, Integer> items = getTraderItems(trader);
        assertFalse(items.isEmpty(), "Trader should have items generated on creation");
    }

    @Test
    void given_PlayerBuysItem_when_TransactionSuccess_then_ItemRemovedFromTrader() throws Exception {
        trader = new Trader("TraderJoe", state, interactions);
        Map<Item, Integer> items = getTraderItems(trader);
        if (items.isEmpty()) fail("Trader has no items to buy");
        Item itemToBuy = items.keySet().iterator().next();
        int initialQty = items.get(itemToBuy);

        when(input.nextLine()).thenReturn("buy " + itemToBuy.getName() + " 1").thenReturn("stop");
        Player player = mock(Player.class);
        Warrior warrior = mock(Warrior.class); 
        when(player.getPlayerCharacter()).thenReturn(warrior);
        when(player.getName()).thenReturn("TestHero");
        trader.talk(state, player, false);

        int newQty = items.get(itemToBuy);
        
        assertEquals(initialQty - 1, newQty, "Trader inventory should decrease by 1");
        verify(warrior).buyItem(any(), eq(player), eq(itemToBuy), eq(1), eq(state));
    }


    private Map<Item, Integer> getTraderItems(Trader trader) throws Exception {
        Field field = Trader.class.getDeclaredField("itemsForSale");
        field.setAccessible(true);
        return (Map<Item, Integer>) field.get(trader);
    }
}
