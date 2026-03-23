package com.questoftherealm.commands;

import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Tile;

public class AttackCommand extends Command {
    public AttackCommand() {
        super("attack");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        String enemyName = args[1];
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        if (curTile == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.error.undefinedArea"));
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.error.noEnemy", enemyName));
            return;
        }
        boolean isKilled = false;
        if(chosenEnemy.isDead()){
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.error.enemyDead", enemyName));
            return;
        }
        try {
            isKilled = chosenEnemy.interact(player,state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.error.battleUnavailable",enemyName));
        }
        if (isKilled) {
            int gold = 5;
            int exp = 10;
            player.addMoney(gold,state);
            player.addExp(exp);
            curTile.removeEnemy(chosenEnemy);
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.success.reward", gold, exp));
        }
        // state.getGameServices().getOutput().println(player.getName() + " attacked " + enemyName + " at " + player.getPosition());
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("attack.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("attack.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }
}