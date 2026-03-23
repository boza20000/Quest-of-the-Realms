package com.questoftherealm.items;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.exceptions.ArmorPieceNotGenerated;
import com.questoftherealm.exceptions.RandomItemNotGenerated;
import com.questoftherealm.exceptions.RandomWeaponNotGenerated;
import com.questoftherealm.game.ConsoleInput;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.LocalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
class ChestTest {

    private GameState state;
    private GameServices services;
    private Chest chest;
    private Player player;
    private ItemRegistry itemRegistry;
    private Input input;

    @BeforeEach
    void setup() throws Exception {
        Output output = new ConsoleOutput();
        input = new ConsoleInput();
        services = new GameServices(output,input);
        state = new GameState("test-room", services);
        player = new Player("Test", PlayerTypes.Warrior, state);
        chest = new Chest(state);
        itemRegistry = new ItemRegistry(new LocalizationService());
        Field f = Chest.class.getDeclaredField("itemRegistry");
        f.setAccessible(true);
        f.set(chest,itemRegistry);
    }


    @Test
    void givenAvailableRegistryItems_whenGenerateRandomItem_thenReturnsValidDrop() {
        ItemDrop drop = chest.generateRandomItem();
        assertNotNull(drop.item(), "Item should be generated");
        assertTrue(drop.quantity() > 0, "Quantity should be more than 0");
        assertNotNull(drop.item().getType());
        assertNotNull(drop.item().getRarity());
    }

    @Test
    void givenWarriorPlayer_whenGenerateRandomWeapon_thenReturnsSwordWeaponDrop() {
        ItemDrop weapon = chest.generateRandomWeapon(player);
        assertNotNull(weapon.item());
        assertEquals(ItemType.WEAPON, weapon.item().getType());
        assertEquals(ItemEffect.SWORD, weapon.item().getEffect());
        assertEquals(1, weapon.quantity());
    }

    @Test
    void givenHelmetRequest_whenGenerateArmorPiece_thenReturnsHelmet() {
        ItemDrop helmet = chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.HELMET);
        assertEquals(ItemEffect.HELMET, helmet.item().getEffect());
    }

    @Test
    void givenChestplateRequest_whenGenerateArmorPiece_thenReturnsChestplate() {
        ItemDrop chestplate = chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.CHESTPLATE);
        assertEquals(ItemEffect.CHESTPLATE, chestplate.item().getEffect());
    }

    @Test
    void givenBootsRequest_whenGenerateArmorPiece_thenReturnsBoots() {
        ItemDrop boots = chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.BOOTS);
        assertEquals(ItemEffect.BOOTS, boots.item().getEffect());
    }

    @Test
    void givenEmptyRegistry_whenGenerateRandomItem_thenThrowsRandomItemNotGenerated() {
        itemRegistry.getAllItems().clear();
        assertThrows(RandomItemNotGenerated.class, () -> chest.generateRandomItem());
    }

    @Test
    void givenNoWeaponsInRegistry_whenGenerateRandomWeapon_thenThrowsRandomWeaponNotGenerated() {
        itemRegistry.getAllItems().removeIf(i -> i.getType() == ItemType.WEAPON);
        assertThrows(RandomWeaponNotGenerated.class, () -> chest.generateRandomWeapon(player));
    }

    @Test
    void givenNoHelmetInRegistry_whenGenerateArmorPiece_thenThrowsArmorPieceNotGenerated() {
        itemRegistry.getAllItems().removeIf(i -> i.getEffect() == ItemEffect.HELMET);
        assertThrows(ArmorPieceNotGenerated.class, () -> chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.HELMET));
    }

    @Test
    void givenNoChestplateInRegistry_whenGenerateArmorPiece_thenThrowsArmorPieceNotGenerated() {
        itemRegistry.getAllItems().removeIf(i -> i.getEffect() == ItemEffect.CHESTPLATE);
        assertThrows(ArmorPieceNotGenerated.class, () -> chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.CHESTPLATE));
    }

    @Test
    void givenNoBootsInRegistry_whenGenerateArmorPiece_thenThrowsArmorPieceNotGenerated() {
        itemRegistry.getAllItems().removeIf(i -> i.getEffect() == ItemEffect.BOOTS);
        assertThrows(ArmorPieceNotGenerated.class, () -> chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.BOOTS));

    }
    @Test
    void givenManyGenerations_whenGenerateRandomItem_thenAlwaysReturnsValidItem() {
        for (int i = 0; i < 1000000; i++) {
            ItemDrop drop = chest.generateRandomItem();
            assertNotNull(drop, "Item drop should not be null on iteration " + i);
            assertNotNull(drop.item(), "Item inside drop should not be null on iteration " + i);
            assertTrue(drop.quantity() > 0, "Quantity should be positive on iteration " + i);
        }
    }
}
