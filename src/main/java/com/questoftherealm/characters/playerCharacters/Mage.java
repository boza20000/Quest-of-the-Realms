package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.SpellCaster;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.spells.Spell;
import com.questoftherealm.items.Item;
import com.questoftherealm.spells.SpellRegister;

import java.util.Scanner;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Mage extends Characters implements SpellCaster {

    public Mage() {
        super(MAGE_HEALTH,
                MAGE_MANA,
                MAGE_ATTACK,
                MAGE_DEFENCE,
                MAGE_ARMOR,
                MAGE_CHARISMA,
                MAGE_SPELLS,
                MAGE_INTELLIGENCE);
    }

    public Mage(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void castSpell(Player player, Spell spell, Enemy target) {
        spell.cast(player, target);
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem("Wooden Staff");
    }

    @Override
    public int getBaseAttack() {
        return MAGE_ATTACK;
    }

    @Override
    public int getBaseDefence() {
        return MAGE_DEFENCE;
    }

    @Override
    public int getMaxHealth() {
        return MAGE_HEALTH;
    }

    @Override
    public void activateAbility(Player player, Enemy enemy) {
        SpellRegister spellRegister = new SpellRegister();
        spellRegister.listSpells();
        System.out.println("Choose a spell to cast: ");
        System.out.print(">");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Spell choice;
        try {
            choice = spellRegister.getSpell(line);
        } catch (Exception e) {
            System.out.println("No such spell");
            return;
        }
        castSpell(player, choice, enemy);
    }
}
