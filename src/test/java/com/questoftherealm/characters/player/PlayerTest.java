package com.questoftherealm.characters.player;


import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.Position;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.LocalizationService;
import com.questoftherealm.localization.MessageBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private Player player;
    private GameState state;
    private Output output;

    private LocalizationService localizationService;
    private ItemRegistry mockItemRegistry;
    private MessageBundle bundle;

    @BeforeEach
    void setup() {

        localizationService = new LocalizationService();
        bundle = new MessageBundle();
        var services = mock(com.questoftherealm.game.GameServices.class);
        output = mock(Output.class);

        when(services.getOutput()).thenReturn(output);

        state = new GameState(null, services);
        mockItemRegistry = mock(ItemRegistry.class);
        when(services.getOutput()).thenReturn(output);

        player = new Player("Hero", PlayerTypes.Warrior, state);
        state.setPlayer(player);

    }
    @Test
    void addExp_IncreasesExperience() {
        int startExp = player.getExperience();
        player.addExp(50);
        assertEquals(startExp + 50, player.getExperience());
    }

    @Test
    void addExp_LevelsUp_WhenExpExceedsThreshold() {
        assertEquals(0,player.getExperience(),"In the start xp sould be zero");
        int expNeeded = player.getLevel() * GameConstants.MAX_EXP_PER_LEVEL;
        player.addExp(expNeeded);
        assertEquals(2, player.getLevel(),"level should increase after we reach max exp on level one");
        assertEquals(0, player.getExperience());
    }

    @Test
    void addExp_CarriesRemainderAfterLevelUp() {
        int expNeeded = player.getLevel() * GameConstants.MAX_EXP_PER_LEVEL;
        player.addExp(expNeeded + 10);
        assertEquals(2, player.getLevel());
        assertEquals(10, player.getExperience(),"after leveling up the remaining xp is added to the next level");
    }

    @Test
    void addExp_CanLevelUpMultipleTimes() {
        player.addExp(player.getLevel() * GameConstants.MAX_EXP_PER_LEVEL * 3);
        assertTrue(player.getLevel() > 2);
    }

    @Test
    void addMoney_IncreasesGold() {
        player.addMoney(100, state);
        assertEquals(100, player.getGold());
    }

    @Test
    void addMoney_DoesNotExceedMaxGold() {
        player.addMoney(GameConstants.MAX_GOLD + 500, state);

        assertEquals(GameConstants.MAX_GOLD, player.getGold());
        verify(output).println(anyString());  // printed max gold message
    }

    @Test
    void payMoney_ReducesGoldIfEnough() {
        player.addMoney(100, state);

        boolean result = player.payMoney(40, state);

        assertTrue(result);
        assertEquals(60, player.getGold());
        verify(output, atLeastOnce()).println(anyString());
    }

    @Test
    void payMoney_ReturnsFalseIfNotEnough() {
        player.addMoney(20, state);

        boolean result = player.payMoney(50, state);

        assertFalse(result);
        assertEquals(20, player.getGold()); // no change
        verify(output, never()).println(anyString());
    }

    @Test
    void payMoney_ZeroCostAlwaysSucceeds() {
        player.addMoney(10, state);

        boolean result = player.payMoney(0, state);

        assertTrue(result);
        assertEquals(10, player.getGold());
        verify(output).println(anyString());
    }

    @Test
    void move_UpdatesXAndY() {
        player.move(5, 7);

        assertEquals(5, player.getX());
        assertEquals(7, player.getY());
    }

    @Test
    void move_UpdatesPositionObject() {
        player.move(2, 3);

        assertEquals(new Position(2, 3), player.getPosition());
    }

    @Test
    void setPosition_UpdatesPosition() {
        Position pos = new Position(8, 9);
        player.setPosition(pos);

        assertEquals(pos, player.getPosition());
    }

    @Test
    void setPosition_UpdatesCoordinates() {
        Position pos = new Position(4, 6);
        player.setPosition(pos);

        assertEquals(4, player.getX());
        assertEquals(6, player.getY());
    }

    @Test
    void setPosition_HandlesZeroCoordinates() {
        Position pos = new Position(0, 0);
        player.setPosition(pos);

        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }
}