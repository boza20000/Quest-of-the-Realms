package com.questoftherealm.game.Commands;

public class EquipCommand extends Command {
    public EquipCommand() {
        super("equip");
    }

    @Override
    public String getDescription() {
        return "equip [item]";
    }

    @Override
    public void execute(String[] args) {

    }
}
