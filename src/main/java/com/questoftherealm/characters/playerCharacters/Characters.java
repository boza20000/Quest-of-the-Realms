package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.EnemyEntities.Enemy;
import com.questoftherealm.characters.characterInterfaces.Combatant;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.TargetNotFound;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemEffect;
import com.questoftherealm.items.ItemType;

import java.util.HashMap;

import static com.questoftherealm.game.GameConstants.*;

public abstract class Characters implements Combatant {

    // Stat bounds from GameConstants
    private int health;        // 0 - MAX_HEALTH
    private int mana;          // 0 - MAX_MANA
    private int attack;        // 0 - MAX_ATTACK
    private int defence;       // 0 - MAX_DEFENCE
    private int armor;         // 0 - MAX_ARMOR
    private int charisma;      // 0 - MAX_CHARISMA
    private int spells;        // 0 - MAX_SPELLS
    private int intelligence;  // 0 - MAX_INTELLIGENCE

    public Characters(Characters other) {
        setHealth(other.getHealth());
        setMana(other.getMana());
        setAttack(other.getAttack());
        setDefence(other.getDefence());
        setArmor(other.getArmor());
        setCharisma(other.getCharisma());
        setIntelligence(other.getIntelligence());
        setSpells(other.getSpells());
    }

    public Characters(int health, int mana, int attack, int defence, int armor,
                      int charisma, int spells, int intelligence) {
        setHealth(health);
        setMana(mana);
        setAttack(attack);
        setDefence(defence);
        setArmor(armor);
        setCharisma(charisma);
        setIntelligence(intelligence);
        setSpells(spells);
    }

    @Override
    public void attack(Enemy target) {
        if (target.isDead()) {
            System.out.println(target.getClass().getSimpleName() + " is already dead!");
            return;
        }
        try {
            target.takeDamage(this.getAttack());
        } catch (TargetNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    public void takeDamage(int damage) {
        int mitigation = defence * 5 + armor;
        int reducedDamage = damage * 100 / (100 + mitigation);
        setHealth(health - reducedDamage);  // uses setter now
        System.out.println(reducedDamage + " damage taken! Health now: " + health + "HP");

        if (isDead()) {
            System.out.println(this.getClass().getSimpleName() + " has died!");
        }
    }

    public boolean isDead() {
        return health == 0;
    }

    // ===== Abstract Weapon =====
    public abstract Item getDefaultWeapon();
    public abstract int getBaseAttack();
    public abstract int getBaseDefence();
    // ===== Getters & Setters =====
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, MAX_HEALTH));
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, MAX_MANA));
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = Math.max(0, Math.min(attack, MAX_ATTACK));
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = Math.max(0, Math.min(defence, MAX_DEFENCE));
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = Math.max(0, Math.min(armor, MAX_ARMOR));
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = Math.max(0, Math.min(charisma, MAX_CHARISMA));
    }

    public int getSpells() {
        return spells;
    }

    public void setSpells(int spells) {
        this.spells = Math.max(0, Math.min(spells, MAX_SPELLS));
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = Math.max(0, Math.min(intelligence, MAX_INTELLIGENCE));
    }

    @Override
    public String toString() {
        return "===== " + this.getClass().getSimpleName() + " Stats =====\n" +
                "Health      : " + getHealth() + "\n" +
                "Mana        : " + getMana() + "\n" +
                "Attack      : " + getAttack() + "\n" +
                "Defence     : " + getDefence() + "\n" +
                "Armor       : " + getArmor() + "\n" +
                "Charisma    : " + getCharisma() + "\n" +
                "Spells      : " + getSpells() + "\n" +
                "Intelligence: " + getIntelligence() + "\n" +
                "==========================";
    }
}
