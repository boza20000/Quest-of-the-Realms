package com.questoftherealm.game.Commands;

import com.questoftherealm.game.Game;

import static com.questoftherealm.game.Game.getPlayer;

public class InventoryCommand extends Command {
    public InventoryCommand() {
        super("inventory");
    }

    @Override
    public String getDescription() {
        return "lists all inventort items";
    }

    @Override
    public void execute(String[] args) {
       getPlayer().openInventory();
    }
}
