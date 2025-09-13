package com.questoftherealm.game.Commands;

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
}
