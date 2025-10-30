package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.MonsterBehavior;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemRegistry;

import java.util.Random;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Orc extends Characters implements MonsterBehavior {

    public Orc() {
        super(ORC_HEALTH,
                ORC_MANA,
                ORC_ATTACK,
                ORC_DEFENCE,
                ORC_ARMOR,
                ORC_CHARISMA,
                ORC_SPELLS,
                ORC_INTELLIGENCE);
    }

    public Orc(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void resurrect() {
        if (isDead()) {
            System.out.println("You close your eyes, calling upon the spirits of your ancestors...");
            System.out.println("💀 Ancient orc spirits answer your plea for resurrection!");

            int roll = new Random().nextInt(10);
            if (roll < 4) { // 60% chance
                setHealth(getMaxHealth());
                System.out.println("👹 The spirits restore your wounds and bring you back to life!");
            } else {
                System.out.println("⚠️ The spirits ignore your call. Be careful, resurrection failed!");
            }
        } else {
            System.out.println("You are still alive. No need to call upon the spirits.");
        }
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Iron Axe");
    }

    @Override
    public int getBaseAttack() {
        return ORC_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return ORC_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return ORC_HEALTH;
    }

    @Override
    public void activateAbility(Player player, Enemy enemy) {
        System.out.println("🪓 You attack with full force head on and strike your enemy with your " + player.getWeapon().getName() + " dealing: ");
        enemy.takeDamage(player.getPlayerCharacter().getAttack() * 2);
    }
}
