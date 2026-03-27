package com.questoftherealm.commands;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Tile;
import com.questoftherealm.server.ServerLogger;

public class AttackCommand extends Command {
    public AttackCommand() {
        super("attack");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player, state)) {
            return;
        }
        String enemyName = args[1];
        Tile curTile = player.curTile(state);
        if (curTile == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.error.undefinedArea"));
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null || chosenEnemy.isDead()) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.error.noEnemy", enemyName));
            return;
        }
        boolean isKilled = false;
        try {
            isKilled = chosenEnemy.interact(player, state);
        } catch (RuntimeException e) {
            ServerLogger.get().warn("AttackCommand: Failed to battle enemy " + enemyName + " for player " + player.getName(), e);
            if (!isKilled) {
                state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.error.battleUnavailable", enemyName));
            }
            return;
        }
        if (isKilled) {
            curTile.removeEnemy(chosenEnemy, state);
        }

    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("attack.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player, GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player, state);
    }
}