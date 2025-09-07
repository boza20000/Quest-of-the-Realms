package com.questoftherealm.game.Commands;

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

    }
}
