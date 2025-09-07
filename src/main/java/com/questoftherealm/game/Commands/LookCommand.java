package com.questoftherealm.game.Commands;

public class LookCommand extends Command {

    public LookCommand() {
        super("look");
    }

    @Override
    public String getDescription() {
        return "looking around for (items,materials or enemies) near the players location";
    }

    @Override
    public void execute(String[] args) {

    }
}
