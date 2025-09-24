package com.questoftherealm.commands;

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
       getPlayer().openInventory();
    }
}
