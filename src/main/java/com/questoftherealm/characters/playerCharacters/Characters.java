package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.characterInterfaces.Combatant;
import com.questoftherealm.exceptions.NotEnoughManaException;
import com.questoftherealm.exceptions.TargetNotFound;
import com.questoftherealm.game.GameState;

import static com.questoftherealm.game.GameConstants.*;

public abstract class Characters implements Combatant {

    private int health;
    private int mana;
    private int attack;
    private int defence;
    private int armor;
    private int charisma;
    private int spells;
    private int intelligence;

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
    public  void attack(Enemy target, Player player, GameState state) {
        if (target.isDead()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("character.target.alreadyDead", target.getClass().getSimpleName()));
            return;
        }
        try {
            useMana(player.getWeapon().getMana());
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("character.attack.hit", target.getClass().getSimpleName(), this.getAttack()));
            target.takeDamage(this.getAttack(), state);
        } catch (TargetNotFound | NotEnoughManaException e) {
            state.getGameServices().getOutput().println(e.getMessage());
        }
    }

    public synchronized void takeDamage(int damage, GameState state,Player player) {
        int mitigation = defence * 5 + armor;
        int reducedDamage = damage * 100 / (100 + mitigation);
        setHealth(health - reducedDamage);
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("character.damage.taken", reducedDamage, health));

        if (isDead()) {
            if(player.getPlayerCharacter() instanceof Orc){
                ((Orc) player.getPlayerCharacter()).resurrect(state);
                return;
            }
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("character.dead", this.getClass().getSimpleName()));
        }
    }

    public synchronized boolean isDead() {
        return health == 0;
    }

    public abstract String getDefaultWeapon(GameState state);

    public abstract int getBaseAttack();

    public abstract int getBaseDefence();

    public abstract int getMaxHealth();

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, this.getMaxHealth()));
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

    public String stats(GameState state) {
        return state.getMessages().getBundle().get("character.stats.header", this.getClass().getSimpleName()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.health", getHealth()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.mana", getMana()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.attack", getAttack()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.defence", getDefence()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.armor", getArmor()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.charisma", getCharisma()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.spells", getSpells()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.intelligence", getIntelligence()) + System.lineSeparator() +
                state.getMessages().getBundle().get("character.stats.footer");
    }

    public synchronized void useMana(int mana) {
        if( getMana() >=mana) {
            setMana(Math.max(0, getMana() - mana));
        }
        else {
            throw new NotEnoughManaException("You don't have enough Mana");
        }
    }

    public abstract void activateAbility(Player player, Enemy enemy, GameState state);

    public void block( Enemy enemy, Player player, GameState state) {
        useMana(player.getWeapon().getMana()/3);
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("character.block.start", enemy.getClass().getSimpleName()));
        int blockAmount = (int) (getDefence() * 1.5);
        int damage = Math.max(enemy.getBaseAttack() - blockAmount, 0);
        takeDamage(damage, state, player);
    }
}
