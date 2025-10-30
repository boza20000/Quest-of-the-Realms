package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.Deceiver;
import com.questoftherealm.characters.characterInterfaces.Trader;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.entities.TraderNPC;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemRegistry;

import java.util.Random;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Rogue extends Characters implements Deceiver {

    public Rogue() {
        super(ROGUE_HEALTH,
                ROGUE_MANA,
                ROGUE_ATTACK,
                ROGUE_DEFENCE,
                ROGUE_ARMOR,
                ROGUE_CHARISMA,
                ROGUE_SPELLS,
                ROGUE_INTELLIGENCE);
    }

    public Rogue(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void pickpocket(Player player) {
        int roll = new Random().nextInt(10);
        if (roll < 6) {//60%
            ItemDrop loot = Chest.generateRandomItem();
            player.getInventory().addItem(loot.item(), loot.quantity());
        } else {
            System.out.println("You attempt to steal failed! Be careful you might get caught");
        }
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Iron Dagger");
    }

    @Override
    public int getBaseAttack() {
        return ROGUE_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return ROGUE_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return ROGUE_HEALTH;
    }

    @Override
    public void activateAbility(Player player, Enemy enemy) {
        System.out.println("🤫 You attempt to pickpocket an unsuspecting " + enemy.getClass().getSimpleName().toUpperCase() + " ...");
        pickpocket(player);
    }

}
