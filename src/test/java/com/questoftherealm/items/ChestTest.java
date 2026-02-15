package com.questoftherealm.items;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.player.PlayerTypes;
import com.questoftherealm.exceptions.ArmorPieceNotGenerated;
import com.questoftherealm.exceptions.RandomItemNotGenerated;
import com.questoftherealm.exceptions.RandomWeaponNotGenerated;
import com.questoftherealm.game.ConsoleOutput;
import com.questoftherealm.game.GameServices;
import com.questoftherealm.game.GameState;
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

    @BeforeEach
    void setup() throws Exception {
        Output output = new ConsoleOutput();
        services = new GameServices(output);
        state = new GameState(player, services);
        chest = new Chest(state);
        itemRegistry = new ItemRegistry(new LocalizationService());
        player = new Player("Test", PlayerTypes.Warrior, state);
        Field f = Chest.class.getDeclaredField("itemRegistry");
        f.setAccessible(true);
        f.set(chest,itemRegistry);
    }


    @Test
    void generateRandomItem_NormalCase() {
        ItemDrop drop = chest.generateRandomItem();
        assertNotNull(drop.item(), "Item should be generated");
        assertTrue(drop.quantity() > 0, "Quantity should be more than 0");
        assertNotNull(drop.item().getType());
        assertNotNull(drop.item().getRarity());
    }

    @Test
    void generateRandomWeapon_Warrior() {
        ItemDrop weapon = chest.generateRandomWeapon(player);
        assertNotNull(weapon.item());
        assertEquals(weapon.item().getType(), ItemType.WEAPON);
        assertEquals(weapon.item().getEffect(), ItemEffect.SWORD);
        assertEquals(1, weapon.quantity());
    }

    @Test
    void generateRandomHelmet() {
        ItemDrop helmet = chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.HELMET);
        assertEquals(ItemEffect.HELMET, helmet.item().getEffect());
    }

    @Test
    void generateRandomChestplate() {
        ItemDrop chestplate = chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.CHESTPLATE);
        assertEquals(ItemEffect.CHESTPLATE, chestplate.item().getEffect());
    }

    @Test
    void generateRandomBoots() {
        ItemDrop boots = chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.BOOTS);
        assertEquals(ItemEffect.BOOTS, boots.item().getEffect());
    }

    @Test
    void generateRandomItem_NoItems_Throws() throws Exception {
        var backup = itemRegistry.getAllItems().stream().toList();
        itemRegistry.getAllItems().clear();
        assertThrows(RandomItemNotGenerated.class, () -> chest.generateRandomItem());
        itemRegistry.getAllItems().addAll(backup);
    }

    @Test
    void generateRandomWeapon_NoWeapon_Throws() {
        var backup = itemRegistry.getAllItems().stream().toList();
        itemRegistry.getAllItems().removeIf(i -> i.getType() == ItemType.WEAPON);
        assertThrows(RandomWeaponNotGenerated.class, () -> chest.generateRandomWeapon(player));
        itemRegistry.getAllItems().addAll(backup);
    }

    @Test
    void generateRandomHelmet_NoHelmet_Throws() {
        var backup = itemRegistry.getAllItems().stream().toList();
        itemRegistry.getAllItems().removeIf(i -> i.getEffect() == ItemEffect.HELMET);
        assertThrows(ArmorPieceNotGenerated.class, () -> chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.HELMET));
        itemRegistry.getAllItems().addAll(backup);
    }

    @Test
    void generateRandomChestplate_NoChestplate_Throws() {
        var backup = itemRegistry.getAllItems().stream().toList();
        itemRegistry.getAllItems().removeIf(i -> i.getEffect() == ItemEffect.CHESTPLATE);
        assertThrows(ArmorPieceNotGenerated.class, () -> chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.CHESTPLATE));
        itemRegistry.getAllItems().addAll(backup);
    }

    @Test
    void generateRandomBoots_NoBoots_Throws() {
        var backup = itemRegistry.getAllItems().stream().toList();
        itemRegistry.getAllItems().removeIf(i -> i.getEffect() == ItemEffect.BOOTS);
        assertThrows(ArmorPieceNotGenerated.class, () -> chest.generateArmorPiece(ItemType.ARMOR,ItemEffect.BOOTS));
        itemRegistry.getAllItems().addAll(backup);
    }
}
