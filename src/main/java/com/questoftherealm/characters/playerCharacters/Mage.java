package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.SpellCaster;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.NoSuchSpell;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.spells.Spell;
import com.questoftherealm.items.Item;
import com.questoftherealm.spells.SpellRegister;
import com.questoftherealm.localization.MessageBundle;

import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Mage extends Characters implements SpellCaster {

    public Mage() {
        super(MAGE_HEALTH, MAGE_MANA, MAGE_ATTACK, MAGE_DEFENCE, MAGE_ARMOR, MAGE_CHARISMA, MAGE_SPELLS, MAGE_INTELLIGENCE);
    }

    public Mage(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void castSpell(Player player, Spell spell, Enemy target, GameState state) {
        spell.cast(player, target, state);
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem(MessageBundle.get("mage.weapon.default"));
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
    public void activateAbility(Player player, Enemy enemy, GameState state) {
        Output output = state.getGameServices().getOutput();
        SpellRegister spellRegister = new SpellRegister(state.getGameServices().getOutput());
        spellRegister.listSpells();
        output.println(MessageBundle.get("mage.spell.choose"));
        output.print(">");
        String line = state.getGameServices().getInput().nextLine();
        Spell choice;
        try {
            choice = spellRegister.getSpell(line);
        } catch (Exception e) {
            throw new NoSuchSpell(MessageBundle.get("mage.spell.notFound"));
        }
        castSpell(player, choice, enemy, state);
    }
}
