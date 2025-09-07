package com.questoftherealm.game.Commands;

public class MoveCommand extends Command {

    public MoveCommand() {
        super("move");
    }

    @Override
    public String getDescription() {
        return "- move [north|south|east|west](direction)";
    }

    @Override
    public void execute(String[] args) {

    }
}
