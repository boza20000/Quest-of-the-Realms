package com.questoftherealm.game.Commands;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("complete Quest");
    }

    @Override
    public void execute(String[] args) {

    }

    @Override
    public String getDescription() {
        return "-shows the progress on the current quest";
    }
}
