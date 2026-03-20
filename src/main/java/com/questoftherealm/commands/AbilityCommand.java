package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.AbilityException;
import com.questoftherealm.exceptions.NoSuchSpell;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Tile;
import com.questoftherealm.server.ServerLogger;

public class AbilityCommand extends Command {
    public AbilityCommand() {
        super("super");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        String enemyName = args[1];
        Tile curTile = player.curTile(state);
        if (curTile == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("ability.error.undefinedArea"));
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("ability.error.noEnemy", enemyName));
            return;
        }

        try {
            player.getPlayerCharacter().activateAbility(player, chosenEnemy, state);
            if (chosenEnemy.isDead()) {
                curTile.removeEnemy(chosenEnemy, state);
            }
        } catch (NoSuchSpell | AbilityException e) {
            ServerLogger.get().error("AbilityCommand: Failed to activate ability for player " + player.getName() + " against enemy " + enemyName, e);
            throw new AbilityException(player.getPlayerCharacter() + state.getMessages().getBundle().get("ability.error.abilityFailed"));
        }

    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("ability.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("ability.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player, state);
    }
}