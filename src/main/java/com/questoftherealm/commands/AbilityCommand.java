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
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("ability.error.undefinedArea"));
            return;
        }
        Enemy chosenEnemy = curTile.getEnemy(enemyName);
        if (chosenEnemy == null) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("ability.error.noEnemy", enemyName));
            return;
        }
        try {
            player.getPlayerCharacter().activateAbility(player, chosenEnemy,state);
        } catch (Exception e) {
            throw new AbilityException(player.getPlayerCharacter() + state.getMessages().getBundle().get("ability.error.abilityFailed"));
        }
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("ability.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 2) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("ability.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }
}