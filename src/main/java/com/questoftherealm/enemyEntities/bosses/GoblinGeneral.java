package com.questoftherealm.enemyEntities.bosses;

import com.questoftherealm.characters.characterInterfaces.Combatant;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemRegistry;

import java.util.HashMap;
import java.util.List;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class GoblinGeneral extends Boss  {
    private GameState state;
    private ItemRegistry itemRegistry;
    private final String NAME = "Azok";

    public GoblinGeneral(GameState state) {
        super(GoblinGeneral_HEALTH,
                GoblinGeneral_MANA,
                GoblinGeneral_ATTACK,
                GoblinGeneral_DEFENCE,
                GoblinGeneral_ARMOR,
                GoblinGeneral_CHARISMA,
                GoblinGeneral_SPELLS,
                GoblinGeneral_INTELLIGENCE,
                null,
                null,
                null,
                null,
                false);
        this.state = state;
        this.itemRegistry = state.getItemRegistry();
        createArmor();
        createLoot();
        this.weapon = itemRegistry.getItem("Big Battle Axe");
        this.name = NAME;
    }

    private HashMap<ItemEffect, Item> createArmor() {
        HashMap<ItemEffect, Item> armor = new HashMap<>();
        armor.put(ItemEffect.HELMET, itemRegistry.getItem("Goblin general’s Helmet"));
        armor.put(ItemEffect.CHESTPLATE, itemRegistry.getItem("Goblin general’s Chestplate"));
        armor.put(ItemEffect.BOOTS, null);
        return armor;
    }

    private List<ItemDrop> createLoot() {
        return List.of(
                new ItemDrop(itemRegistry.getItem("Goblin general’s Helmet"), 1),
                new ItemDrop(itemRegistry.getItem("Big Battle Axe"), 1)
        );
    }

    @Override
    public void superMove(Player player, GameState state) {
        int newHealth = getHealth() * 2;
        setHealth(newHealth);
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("boss.goblinGeneral.superMove", NAME));
    }

    @Override
    public String getDefaultWeapon(GameState state) {
        return state.getMessages().getBundle().get(this.weapon.getName());
    }

    @Override
    public int getBaseAttack() {
        return GoblinGeneral_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return GoblinGeneral_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return GoblinGeneral_HEALTH;
    }

    @Override
    public void activateAbility(Player player, Enemy enemy, GameState state) {
        superMove(player, state);
    }

    public String getName() {
        return NAME;
    }
}
