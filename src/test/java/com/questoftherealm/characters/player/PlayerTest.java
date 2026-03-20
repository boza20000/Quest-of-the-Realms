package com.questoftherealm.characters.player;


import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.Position;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemType;
import com.questoftherealm.items.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerTest {

    private Player player;
    private GameState state;
    private Output output;

    @BeforeEach
    void setup() {
        var services = mock(com.questoftherealm.game.GameServices.class);
        output = mock(Output.class);

        when(services.getOutput()).thenReturn(output);

        state = new GameState("test-room", services);
        when(services.getOutput()).thenReturn(output);

        player = new Player("Hero", PlayerTypes.Warrior, state);
        state.addPlayer(player);

    }

    @Test
    void givenExperienceGain_whenAddExp_thenExperienceIncreases() {
        int startExp = player.getExperience();
        player.addExp(50);
        assertEquals(startExp + 50, player.getExperience());
    }

    @Test
    void givenThresholdExperience_whenAddExp_thenPlayerLevelsUp() {
        assertEquals(0, player.getExperience(), "In the start xp sould be zero");
        int expNeeded = player.getLevel() * GameConstants.MAX_EXP_PER_LEVEL;
        player.addExp(expNeeded);
        assertEquals(2, player.getLevel(), "level should increase after we reach max exp on level one");
        assertEquals(0, player.getExperience());
    }

    @Test
    void givenOverflowExperience_whenAddExp_thenRemainderCarriesToNextLevel() {
        int expNeeded = player.getLevel() * GameConstants.MAX_EXP_PER_LEVEL;
        player.addExp(expNeeded + 10);
        assertEquals(2, player.getLevel());
        assertEquals(10, player.getExperience(), "after leveling up the remaining xp is added to the next level");
    }

    @Test
    void givenLargeExperienceGain_whenAddExp_thenCanLevelUpMultipleTimes() {
        player.addExp(player.getLevel() * GameConstants.MAX_EXP_PER_LEVEL * 3);
        assertTrue(player.getLevel() > 2);
    }

    @Test
    void givenGoldAmount_whenAddMoney_thenGoldIncreases() {
        player.addMoney(100, state);
        assertEquals(100, player.getGold());
    }

    @Test
    void givenAmountAboveMax_whenAddMoney_thenGoldCapsAtMax() {
        player.addMoney(GameConstants.MAX_GOLD + 500, state);

        assertEquals(GameConstants.MAX_GOLD, player.getGold());
        verify(output).println(anyString());  // printed max gold message
    }

    @Test
    void givenEnoughGold_whenPayMoney_thenGoldIsReducedAndReturnsTrue() {
        player.addMoney(100, state);

        boolean result = player.payMoney(40, state);

        assertTrue(result);
        assertEquals(60, player.getGold());
        verify(output, atLeastOnce()).println(anyString());
    }

    @Test
    void givenInsufficientGold_whenPayMoney_thenReturnsFalseAndGoldUnchanged() {
        player.addMoney(20, state);

        boolean result = player.payMoney(50, state);

        assertFalse(result);
        assertEquals(20, player.getGold()); // no change
        verify(output, never()).println(anyString());
    }

    @Test
    void givenZeroCost_whenPayMoney_thenAlwaysSucceeds() {
        player.addMoney(10, state);

        boolean result = player.payMoney(0, state);

        assertTrue(result);
        assertEquals(10, player.getGold());
        verify(output).println(anyString());
    }

    @Test
    void givenCoordinates_whenMove_thenUpdatesXAndY() {
        player.move(5, 7);

        assertEquals(5, player.getX());
        assertEquals(7, player.getY());
    }

    @Test
    void givenCoordinates_whenMove_thenUpdatesPositionObject() {
        player.move(2, 3);

        assertEquals(new Position(2, 3), player.getPosition());
    }

    @Test
    void givenPosition_whenSetPosition_thenPositionIsUpdated() {
        Position pos = new Position(8, 9);
        player.setPosition(pos);

        assertEquals(pos, player.getPosition());
    }

    @Test
    void givenPosition_whenSetPosition_thenCoordinatesAreUpdated() {
        Position pos = new Position(4, 6);
        player.setPosition(pos);

        assertEquals(4, player.getX());
        assertEquals(6, player.getY());
    }

    @Test
    void givenZeroCoordinates_whenSetPosition_thenCoordinatesAreZero() {
        Position pos = new Position(0, 0);
        player.setPosition(pos);

        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    void givenPlayerInitialization_whenGetName_thenReturnsCorrectName() {
        assertEquals("Hero", player.getName());
    }

    @Test
    void givenPlayerInitialization_whenGetPlayerType_thenReturnsCorrectType() {
        assertEquals(PlayerTypes.Warrior, player.getPlayerType());
    }

    @Test
    void givenPlayerInitialization_whenGetCurrentZone_thenReturnsSpawnZone() {
        assertEquals(GameConstants.PLAYER_SPAWN, player.getCurrentZone());
    }

    @Test
    void givenNewZone_whenSetCurrentZone_thenCurrentZoneIsUpdated() {
        String newZone = "Dark Forest";
        player.setCurrentZone(newZone);
        assertEquals(newZone, player.getCurrentZone());
    }

    @Test
    void givenInitialState_whenCheckIsDead_thenReturnsFalse() {
        assertFalse(player.isDead());
    }

    @Test
    void givenPlayerIsKilled_whenSetDead_thenIsDeadReturnsTrue() {
        player.setDead();
        assertTrue(player.isDead());
    }

    @Test
    void givenInitialState_whenCheckIsActive_thenReturnsTrue() {
        assertTrue(player.isActive());
    }

    @Test
    void givenInactiveState_whenSetActive_thenIsActiveReturnsFalse() {
        player.setActive(false);
        assertFalse(player.isActive());
    }

    @Test
    void givenInitialMana_whenLoseMana_thenManaDecreases() {
        int initialMana = player.getCharacterMana();
        int manaToLose = 10;
        if (initialMana < manaToLose) {
            player.getPlayerCharacter().setMana(manaToLose + 10);
            initialMana = player.getCharacterMana();
        }
        player.loseMana(manaToLose);
        assertEquals(initialMana - manaToLose, player.getCharacterMana());
    }

    @Test
    void givenManaPotion_whenUseItem_thenManaIncreases() {
        player.getPlayerCharacter().setMana(0);
        int manaBefore = player.getCharacterMana();

        Item manaPotion = new Item("Mana Potion", ItemType.POTION, true, 10, 5, 0, Rarity.COMMON, ItemEffect.RESTORE_MANA);
        player.useItem(manaPotion);

        assertEquals(Math.min(manaBefore + 10, GameConstants.MAX_MANA), player.getCharacterMana());
    }

    @Test
    void givenHealingPotion_whenUseItem_thenHealthIncreases() {
        player.getPlayerCharacter().setHealth(10);
        int healthBefore = player.getCharacterHealth();

        Item healthPotion = new Item("Healing Potion", ItemType.POTION, true, 20, 10, 0, Rarity.COMMON, ItemEffect.RESTORE_HP);
        player.useItem(healthPotion);

        assertEquals(Math.min(healthBefore + 20, GameConstants.MAX_HEALTH), player.getCharacterHealth());
    }

    @Test
    void givenStrengthPotion_whenUseItem_thenAttackIncreases() {
        int attackBefore = player.getPlayerCharacter().getAttack();

        Item strPotion = new Item("Strength Potion", ItemType.POTION, true, 5, 10, 0, Rarity.COMMON, ItemEffect.BUFF_STRENGTH);
        player.useItem(strPotion);

        assertEquals(Math.min(attackBefore + 5, GameConstants.MAX_ATTACK / 2), player.getPlayerCharacter().getAttack());
    }

    @Test
    void givenWeaponItem_whenEquipWeapon_thenWeaponIsSet() {
        Item sword = new Item("Iron Sword", ItemType.WEAPON, false, 15, 50, 0, Rarity.COMMON, null);

        player.equipWeapon(sword, state);

        assertEquals(sword, player.getWeapon());
    }

    @Test
    void givenArmorItem_whenEquipArmor_thenArmorIsSetInCorrectSlot() {
        Item helmet = new Item("Leather Helmet", ItemType.ARMOR, false, 5, 20, 0, Rarity.COMMON, ItemEffect.HELMET);

        player.equipArmorPiece(helmet, state);

        Map<ItemEffect, Item> armor = player.getArmor();
        assertEquals(helmet, armor.get(ItemEffect.HELMET));
    }

    @Test
    void givenWeaponEquipped_whenRecalculateStats_thenAttackReflectsWeaponPower() {
        int baseAttack = player.getPlayerCharacter().getBaseAttack();
        Item sword = new Item("Iron Sword", ItemType.WEAPON, false, 10, 50, 0, Rarity.COMMON, null);

        player.setWeapon(sword);
        player.recalculateStats();

        assertEquals(baseAttack + 10, player.getPlayerCharacter().getAttack());
    }

    @Test
    void givenArmorEquipped_whenRecalculateStats_thenDefenseReflectsArmorPower() {
        Item chestplate = new Item("Iron Chestplate", ItemType.ARMOR, false, 20, 100, 0, Rarity.COMMON, ItemEffect.CHESTPLATE);

        player.getArmor().put(ItemEffect.CHESTPLATE, chestplate);
        player.recalculateStats();

        assertEquals(20, player.getPlayerCharacter().getArmor());
    }

    @Test
    void givenPlayTimeUpdates_whenSetPlayTime_thenPlayTimeIsStoredCorrectly() {
        long addedTime = 61 * 60 * 1000; // 61 minutes in ms
        player.setPlayTime(addedTime);

        PlayTime pt = player.getPlayTime();
        assertEquals(1, pt.hours());
        assertEquals(1, pt.minutes());
    }

    @Test
    void givenInventoryAccess_whenGetInventory_thenReturnsInventoryObject() {
        assertNotNull(player.getInventory());
    }
}



