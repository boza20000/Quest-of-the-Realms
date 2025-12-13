package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Inventory;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipCommandTest {

    private EquipCommand command;
    private Player player;
    private GameState state;
    private GameServices services;
    private Output output;
    private ItemRegistry registry;

    @BeforeEach
    void setup() {
        player = spy(new Player(
                "TestHero",
                PlayerTypes.Warrior,
                1, 0, 0,
                GameConstants.Castle.x(),
                GameConstants.Castle.y(),
                "Castle",
                null,
                null,
                new Inventory(GameConstants.MAX_ITEMS_IN_INVENTORY),
                null,
                null,
                false
        ));
        player.getInventory().clear();
        player.setWeapon(null);

        services = mock(GameServices.class);
        output = mock(Output.class);
        GameState s = new GameState(player,services);
        state = spy(s);

        when(state.getGameServices()).thenReturn(services);
        when(services.getOutput()).thenReturn(output);
        command = new EquipCommand();
        registry= state.getItemRegistry();

    }

    @Test
    void equipsItemSuccessfully_WhenItemExistsInInventory() throws ItemNotFound {

        Item sword = registry.getItem("Bronze Sword");
        player.getInventory().addItem(sword, 1, state);
        String[] args = {"equip", "Bronze Sword"};
        command.execute(args, player, state);
        assertSame(sword, player.getWeapon(), "Player should equip the sword");
        verify(output).println("Bronze Sword has been equipped successfully!");
    }

    @Test
    void printsError_WhenItemExistsButNotInInventory() throws ItemNotFound {
        assertNotNull(state.getMessages().getBundle());

        Item sword = registry.getItem("Bronze Sword");
        player.getInventory().clear();
        command.execute(new String[]{"equip", "Bronze Sword"}, player, state);
        String expected = state.getMessages().getBundle()
                .get("equip.error.notInInventory", "Bronze Sword");
        assertNotSame(sword, player.getWeapon(), "Player should not equip an item not in inventory");
        verify(output).println(expected);
    }

    @Test
    void printsError_WhenItemDoesNotExist() {
        command.execute(new String[]{"equip", "Fake Sword"}, player, state);
        verify(output).println("This item doesn't exist.");
    }

    @Test
    void printsUsage_WhenMissingArguments() {
        command.execute(new String[]{"equip"}, player, state);
        verify(output).println("Usage: " + command.getDescription(state));
    }

    @Test
    void printsError_WhenPlayerIsNull() {
        command.execute(new String[]{"equip", "Bronze Sword"}, null, state);
        verify(output).println("Error: No player loaded.");
    }
}
