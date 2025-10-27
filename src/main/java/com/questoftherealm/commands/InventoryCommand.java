package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;

import static com.questoftherealm.game.Game.getPlayer;

public class InventoryCommand extends Command {
    public InventoryCommand() {
        super("inventory");
    }

    @Override
    public String getDescription() {
        return "inventory — displays all items in your inventory";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 1) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if (!makeSafe(args, player)) {
            return;
        }
        player.openInventory();
    }
}
