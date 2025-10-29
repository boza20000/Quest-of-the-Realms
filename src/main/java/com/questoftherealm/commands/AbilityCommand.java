package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.game.Game;
import com.questoftherealm.map.Tile;

public class AbilityCommand extends Command {
    public AbilityCommand() {
        super("super");
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if (!makeSafe(args, player)) {
            return;
        }
        String enemyName = args[1];
        Tile curTile = Game.getGameMap().curZone(player.getX(), player.getY());
        if (curTile == null) {
            System.out.println("You are in an undefined area.");
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null) {
            System.out.println("No enemy named '" + enemyName + "' here!");
            return;
        }
        try {
            player.getPlayerCharacter().activateAbility(player, chosenEnemy);
        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }

    @Override
    public String getDescription() {
        return "ability [enemy] - activates player character special ability on the nearest target";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 2) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }
}
