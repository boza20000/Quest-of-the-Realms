package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemType;
import com.questoftherealm.items.Rarity;
import com.questoftherealm.friendlyEntities.Entities.Trader;
import com.questoftherealm.characters.player.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


class CharacterTypeTest {

    private GameState state;
    private GameServices services;
    private Input input;
    private Output output;
    private Player player;
    private RandomService randomService;

    @BeforeEach
    void setup() {
        output = mock(Output.class);
        input = mock(Input.class);
        randomService = mock(RandomService.class);
        services = new GameServices(output, input, randomService);
        state = new GameState("test-room", services);
        // Default player
        player = new Player("Hero", PlayerTypes.Warrior, state);
    }

    @Test
    void verifyWarriorCreationAndStats() {
        Warrior warrior = new Warrior();

        assertEquals(WARRIOR_HEALTH, warrior.getMaxHealth());
        assertEquals(WARRIOR_ATTACK, warrior.getBaseAttack());
        assertEquals(WARRIOR_DEFENCE, warrior.getBaseDefence());
    }

    @Test
    void verifyDetailedWarriorCreation() {
        Warrior warrior = new Warrior(100, 50, 20, 10, 5, 5, 0, 5);

        assertEquals(WARRIOR_HEALTH, warrior.getHealth(), "Health should be clamped to Warrior max health");
        assertEquals(20, warrior.getAttack());
    }

    @Test
    void verifyStatsClampedToGameConstants() {
        Warrior warrior = new Warrior(WARRIOR_HEALTH, WARRIOR_MANA, 50, 50, 100, 5, 0, 5);

        assertEquals(GameConstants.MAX_ATTACK, warrior.getAttack(), "Attack should be clamped to MAX_ATTACK");
        assertEquals(GameConstants.MAX_DEFENCE, warrior.getDefence(), "Defence should be clamped to MAX_DEFENCE");
        assertEquals(GameConstants.MAX_ARMOR, warrior.getArmor(), "Armor should be clamped to MAX_ARMOR");
    }

    @Test
    void verifyWarriorAbility() {
        player = new Player("Hero", PlayerTypes.Warrior, state);
        Enemy enemy = mock(Enemy.class);
        when(enemy.isDead()).thenReturn(false);

        player.getPlayerCharacter().activateAbility(player, enemy, state);

        verify(enemy).takeDamage(eq(WARRIOR_ATTACK * 2), eq(state));
        verify(output, atLeastOnce()).println(anyString());
    }

    @Test
    void verifyOrcCreationAndStats() {
        Orc orc = new Orc();
        assertEquals(ORC_HEALTH, orc.getMaxHealth());
        assertEquals(ORC_ATTACK, orc.getBaseAttack());
    }

    @Test
    void verifyOrcAbility() {
        player = new Player("OrcHero", PlayerTypes.Orc, state);
        Enemy enemy = mock(Enemy.class);
        when(enemy.isDead()).thenReturn(false);

        player.getPlayerCharacter().activateAbility(player, enemy, state);

        verify(enemy).takeDamage(eq(ORC_ATTACK * 2), eq(state));
    }

    @Test
    void verifyMageCreationAndStats() {
        Mage mage = new Mage();
        assertEquals(MAGE_HEALTH, mage.getMaxHealth());
        assertEquals(MAGE_ATTACK, mage.getBaseAttack());
    }

    @Test
    void verifyMageAbility() {
        player = new Player("MageHero", PlayerTypes.Mage, state);
        Enemy enemy = mock(Enemy.class);
        when(enemy.isDead()).thenReturn(false);
        when(input.nextLine()).thenReturn("fireball");

        player.getPlayerCharacter().activateAbility(player, enemy, state);

        verify(output, atLeastOnce()).println(contains("spell"));
        verify(enemy, atLeastOnce()).takeDamage(anyInt(), eq(state));
    }

    @Test
    void verifyRogueCreationAndStats() {
        Rogue rogue = new Rogue();
        assertEquals(ROGUE_HEALTH, rogue.getMaxHealth());
        assertEquals(ROGUE_ATTACK, rogue.getBaseAttack());
    }

    @Test
    void verifyRogueAbility() {
        player = new Player("RogueHero", PlayerTypes.Rogue, state);
        Enemy enemy = mock(Enemy.class);
        when(enemy.isDead()).thenReturn(false);

        when(randomService.randomInt(10)).thenReturn(2);

        player.getPlayerCharacter().activateAbility(player, enemy, state);
        verify(output, atLeastOnce()).println(contains("successfully"));
        verify(output, atLeastOnce()).println(anyString());
    }

    @Test
    void givenOrcNotDead_whenResurrect_thenFails() {
        player = new Player("OrcHero", PlayerTypes.Orc, state);
        Orc playerOrc = (Orc) player.getPlayerCharacter();
        playerOrc.setHealth(10);

        playerOrc.resurrect(state);
        verify(output, atLeastOnce()).println(contains("You are still alive"));
    }

    @Test
    void givenOrcDeadAndLuckyRoll_whenResurrect_thenSuccess() {
        player = new Player("OrcHero", PlayerTypes.Orc, state);
        Orc playerOrc = (Orc) player.getPlayerCharacter();
        playerOrc.setHealth(0);

        when(randomService.randomInt(10)).thenReturn(3);
        playerOrc.resurrect(state);

        assertEquals(playerOrc.getMaxHealth(), playerOrc.getHealth());
        verify(output, atLeastOnce()).println(argThat(msg -> msg.contains("bring you back to life") || msg.contains("spirits answer your plea for resurrection")));
    }

    @Test
    void givenOrcDeadAndUnluckyRoll_whenResurrect_thenStaysDead() {
        player = new Player("OrcHero", PlayerTypes.Orc, state);
        Orc playerOrc = (Orc) player.getPlayerCharacter();
        playerOrc.setHealth(0);
        when(randomService.randomInt(10)).thenReturn(5);
        playerOrc.resurrect(state);

        assertEquals(0, playerOrc.getHealth());
        verify(output).println(contains("failed"));
    }

    @Test
    void givenWarriorWithMoney_whenBuyItem_thenItemAddedAndGoldReduced() {
        player = new Player("WarHero", PlayerTypes.Warrior, state);
        Warrior warrior = (Warrior) player.getPlayerCharacter();

        Item sword = new Item("Silver Sword", ItemType.WEAPON, false, 20, 100, 0, Rarity.UNCOMMON, null);

        player.addMoney(200, state);
        int initialGold = player.getGold();

        warrior.buyItem(mock(Trader.class), player, sword, 1, state);

        assertEquals(initialGold - 100, player.getGold());
        assertTrue(player.getInventory().getItems().containsKey(sword));
        assertEquals(1, player.getInventory().getItems().get(sword));
    }

    @Test
    void givenWarriorWithoutMoney_whenBuyItem_thenPurchaseFails() {
        player = new Player("WarHero", PlayerTypes.Warrior, state);
        Warrior warrior = (Warrior) player.getPlayerCharacter();

        Item sword = new Item("Expensive Sword", ItemType.WEAPON, false, 50, 1000, 0, Rarity.RARE, null);

        player.addMoney(100, state);
        int initialGold = player.getGold();

        warrior.buyItem(mock(Trader.class), player, sword, 1, state);

        assertEquals(initialGold, player.getGold());
        assertFalse(player.getInventory().getItems().containsKey(sword));
        verify(output).println(contains("enough money"));
    }

    @Test
    void givenWarriorInventoryFull_whenBuyItem_thenPurchaseFails() {
        player = new Player("WarHero", PlayerTypes.Warrior, state);
        Warrior warrior = (Warrior) player.getPlayerCharacter();

        Inventory inv = player.getInventory();
        for (int i = 0; i < GameConstants.MAX_ITEMS_IN_INVENTORY; i++) {
            inv.addItem(new Item("Filler " + i, ItemType.CONSUMABLES, false, 0, 0, 0, Rarity.COMMON, null), 1, state);
        }

        Item potion = new Item("Potion", ItemType.POTION, false, 0, 10, 0, Rarity.COMMON, null);
        player.addMoney(100, state);

        warrior.buyItem(mock(Trader.class), player, potion, 1, state);

        assertFalse(inv.getItems().containsKey(potion));
        verify(output).println(contains("space"));
    }
}
