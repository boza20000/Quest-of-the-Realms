package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;

public abstract class Command {
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(String[] args);

    public abstract String getDescription();

    public abstract boolean makeSafe(String[] args,Player player);

    public boolean playerBaseCheck(Player player) {
        if (player == null) {
            System.out.println("Error: No player loaded.");
            return false;
        }
        return true;
    }


}
