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
//Available commands:
//        - move [north|south|east|west]
//        - look
//- attack [enemy]
//        - use [item]
//        - inventory
//- equip [item]
//        - quests
//- complete quest
//- save
//- load
//- exit