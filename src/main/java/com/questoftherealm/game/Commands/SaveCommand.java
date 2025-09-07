package com.questoftherealm.game.Commands;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save");
    }

    @Override
    public String getDescription() {
        return "saves the game progress";
    }

    @Override
    public void execute(String[] args) {

    }
}
