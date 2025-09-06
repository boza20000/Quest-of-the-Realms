package com.questoftherealm.characters;

import com.questoftherealm.characters.interfaces.Combatant;
import com.questoftherealm.characters.interfaces.Explorer;
import com.questoftherealm.characters.interfaces.InventoryHandler;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.maps.Map;

import static com.questoftherealm.characters.GameConstants.*;
import static com.questoftherealm.items.Chest.generateRandomItem;

public abstract class Characters implements Explorer, InventoryHandler, Combatant {
    private int health;//0-50
    private int mana;//0-30
    private int attack;//0-10
    private int defence;//0-5
    private int armor;//0-20
    private int charisma;//0-10
    private int spells;//0-10
    private int intelligence;//0-10

    public Characters(Characters characters) {
        setHealth(characters.getHealth());
        setMana(characters.getMana());
        setAttack(characters.getAttack());
        setDefence(characters.getDefence());
        setArmor(characters.getArmor());
        setCharisma(characters.getCharisma());
        setIntelligence(characters.getIntelligence());
        setSpells(characters.getSpells());
    }

    public Characters(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        setHealth(health);
        setMana(mana);
        setAttack(attack);
        setDefence(defence);
        setArmor(armor);
        setCharisma(charisma);
        setIntelligence(intelligence);
        setSpells(spells);
    }

    public void attack(Characters target) {
        target.takeDamage(this.getAttack());
    }

    public void takeDamage(int damage) {
        int mitigation = defence * 5 + armor;
        int reducedDamage = damage * 100 / (100 + mitigation);
        health -= reducedDamage;
        if (health < 0) this.health = 0;
        System.out.println(reducedDamage + " damage taken! Health now: \"" + this.health + "HP");
    }


    public void move(int x, int y) {
        Map map = Map.getInstance();
        map.movePlayer(Game.getPlayer(), x, y);
    }

    public void openChest() {
        ItemDrop drop = generateRandomItem();
        System.out.println("Chest opened");
        System.out.println("Random item drop: " + drop.item() + "x" + drop.quantity());
        Game.getPlayer().getInventory().addItem(drop.item(), drop.quantity());
    }

    public void addItem(Item item, int quantity) {
        Game.getPlayer().getInventory().addItem(item, quantity);
    }

    public void openInventory() {
        Game.getPlayer().getInventory().listItems();
    }

    public void useItem(Item item) {
        Characters curCharacter = Game.getPlayer().getPlayerCharacter();

        switch (item.getEffect()) {
            case RESTORE_MANA -> curCharacter.setMana(Math.min(item.getPower() + curCharacter.getMana(), MAX_MANA));
            case BUFF_STRENGTH ->
                    curCharacter.setAttack(Math.min(curCharacter.getAttack() + item.getPower(), MAX_ATTACK));
            case SWORD, AXE, DAGGER, STAFF, BOW, DEFENCE -> curCharacter.equipItem(item);
//                case SPELL_FIRE -> curCharacter.castSpell("fireball", item.getPower());
//                case SPELL_ICE -> curCharacter.castSpell("iceSpike", item.getPower());
//                case SPELL_HEAL -> curCharacter.castSpell("heal", item.getPower());
//                case SPELL_SHIELD -> curCharacter.castSpell("shield", item.getPower());
//                case SPELL_LIGHTNING -> curCharacter.castSpell("lightning", item.getPower());
            case RESTORE_HP -> curCharacter.setHealth(Math.min(curCharacter.getHealth() + item.getPower(), MAX_HEALTH));
//                case BUFF_CHARISMA -> curCharacter.addBuff("charisma", item.getPower());
//              case BUFF_INTELLIGENCE -> curCharacter.addBuff("intelligence", item.getPower());
//                case QUEST_ITEM -> Game.getQuestManager().collectItem(item);
//                case FRAGMENT -> Game.getFragmentManager().collectFragment(item);
        }


    }

    public void equipItem(Item item) {
        Characters curCharacter = Game.getPlayer().getPlayerCharacter();
        switch (item.getType()) {
            case ARMOR -> {
                curCharacter.setArmor(Math.min(MAX_ARMOR, curCharacter.getArmor() + item.getPower()));
                //Game.getPlayer().addArmorPiece(item);
            }

            case WEAPON -> curCharacter.setAttack(Math.min(MAX_ATTACK, curCharacter.getAttack() + item.getPower()));
            //Game.getPlayer().addWeapon(item);
        }
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > 0 && health <= 50) {
            this.health = health;
        }
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        if (intelligence > 0 && intelligence <= 10) {
            this.intelligence = intelligence;
        }
    }

    public int getSpells() {
        return spells;
    }

    public void setSpells(int spells) {
        if (spells > 0 && spells <= 10) {
            this.spells = spells;
        }
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        if (charisma > 0 && charisma <= 10) {
            this.charisma = charisma;
        }
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        if (armor > 0 && armor <= 20) {
            this.armor = armor;
        }
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        if (defence > 0 && defence <= 5) {
            this.defence = defence;
        }
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        if (mana > 0 && mana <= 30) {
            this.mana = mana;
        }
        this.mana = mana;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        if (attack > 0 && attack <= 10) {
            this.attack = attack;
        }
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
                "=================================";
    }
}
