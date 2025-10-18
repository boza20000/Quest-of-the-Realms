package com.questoftherealm.commands;

import com.questoftherealm.game.Game;

import static com.questoftherealm.game.Game.getPlayer;

public class InventoryCommand extends Command {
    public InventoryCommand() {
        super("inventory");
    }

    @Override
    public String getDescription() {
        return "lists all inventory items";
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            System.out.println("Usage: inventory");
            return;
        }
        if (Game.getPlayer() == null) {
            System.out.println("Error: No player currently active.");
            return;
        }
        getPlayer().openInventory();
    }
}
