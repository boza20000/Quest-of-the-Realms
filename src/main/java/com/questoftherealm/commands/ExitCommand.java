package com.questoftherealm.commands;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit");
    }

    @Override
    public String getDescription() {
        return "closes the game and saves it automatically";
    }

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
}
