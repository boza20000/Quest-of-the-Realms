package com.questoftherealm.characters.playerCharacters;

import com.questoftherealm.characters.characterInterfaces.Deceiver;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;
import java.util.Random;
import static com.questoftherealm.characters.playerCharacters.CharacterConstants.*;

public class Rogue extends Characters implements Deceiver {

    public Rogue() {
        super(ROGUE_HEALTH, ROGUE_MANA, ROGUE_ATTACK, ROGUE_DEFENCE, ROGUE_ARMOR, ROGUE_CHARISMA, ROGUE_SPELLS, ROGUE_INTELLIGENCE);
    }

    public Rogue(int health, int mana, int attack, int defence, int armor, int charisma, int spells, int intelligence) {
        super(health, mana, attack, defence, armor, charisma, spells, intelligence);
    }

    @Override
    public void pickpocket(Player player, Enemy enemy, GameState state) {
        int roll = new Random().nextInt(10);
        if (roll < 6) {
            Chest chest = new Chest(state);
            ItemDrop loot = chest.generateRandomItem();
            player.getInventory().addItem(loot.item(), loot.quantity(),state);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("rouge.pickpocket.successful",enemy.getClass().getSimpleName()));
        } else {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("rogue.pickpocket.fail"));
        }
    }

    @Override
    public String getDefaultWeapon(GameState state) {
        return state.getMessages().getBundle().get("rogue.weapon.default");
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
    public void activateAbility(Player player, Enemy enemy,GameState state) {
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("rogue.ability.start", enemy.getClass().getSimpleName().toUpperCase()));
        if(enemy.isDead() ){//check if enemy can be pick-pocketed
            return;
        }
        pickpocket(player,enemy,state);
    }
}
