package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.SpellCaster;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.NoSuchSpell;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.spells.Spell;
import com.questoftherealm.spells.SpellRegister;

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
    public String getDefaultWeapon(GameState state) {
        return state.getMessages().getBundle().get("mage.weapon.default");
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
        SpellRegister spellRegister = new SpellRegister(state);
        spellRegister.listSpells();
        printMessage(output,state.getMessages().getBundle().get("mage.spell.choose"));
        String line = state.getGameServices().getInput().nextLine();
        Spell choice;
        try {
            choice = spellRegister.getSpell(line);
        } catch (Exception e) {
            throw new NoSuchSpell(state.getMessages().getBundle().get("mage.spell.notFound"));
        }
        castSpell(player, choice, enemy, state);
    }

    private void printMessage(Output output,String message) {
        output.println(message);
        output.print(">");
        output.flush();
    }

}
