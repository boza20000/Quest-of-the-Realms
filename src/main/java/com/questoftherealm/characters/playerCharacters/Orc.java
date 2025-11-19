package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.MonsterBehavior;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;
import java.util.Random;
import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Orc extends Characters implements MonsterBehavior {

    public Orc() {
        super(ORC_HEALTH, ORC_MANA, ORC_ATTACK, ORC_DEFENCE, ORC_ARMOR, ORC_CHARISMA, ORC_SPELLS, ORC_INTELLIGENCE);
    }

    public Orc(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void resurrect(GameState state) {
        if (isDead()) {
            state.getGameServices().getOutput().println(MessageBundle.get("orc.resurrect.call"));
            state.getGameServices().getOutput().println(MessageBundle.get("orc.resurrect.spiritsSummoned"));

            int roll = new Random().nextInt(10);
            if (roll < 4) {
                setHealth(getMaxHealth());
                state.getGameServices().getOutput().println(MessageBundle.get("orc.resurrect.success"));
            } else {
                state.getGameServices().getOutput().println(MessageBundle.get("orc.resurrect.fail"));
            }
        } else {
            state.getGameServices().getOutput().println(MessageBundle.get("orc.resurrect.notDead"));
        }
    }

    @Override
    public Item getDefaultWeapon() {
        return ItemRegistry.getItem(MessageBundle.get("orc.weapon.default"));
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
    public void activateAbility(Player player, Enemy enemy, GameState state) {
        state.getGameServices().getOutput().println(MessageBundle.get("orc.ability.attack", player.getWeapon().getName()));
        enemy.takeDamage(player.getPlayerCharacter().getAttack() * 2,state);
    }
}
