package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.exceptions.AbilityException;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;
import com.questoftherealm.map.Tile;

public class AbilityCommand extends Command {
    public AbilityCommand() {
        super("super");
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        String enemyName = args[1];
        Tile curTile = state.getMap().curZone(player.getX(), player.getY());
        if (curTile == null) {
            state.getGameServices().getOutput().println("You are in an undefined area.");
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null) {
            state.getGameServices().getOutput().println("No enemy named '" + enemyName + "' here!");
            return;
        }
        try {
            player.getPlayerCharacter().activateAbility(player, chosenEnemy,state);
        } catch (Exception e) {
            throw new AbilityException(player.getPlayerCharacter() + "ability failed.");
        }
    }

    @Override
    public String getDescription() {
        return "ability [enemy] - activates player character special ability on the nearest target";
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }
}
