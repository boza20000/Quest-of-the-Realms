package com.questoftherealm.game.Commands;

public class CompleteQuestCommand extends Command {
    public CompleteQuestCommand() {
        super("complete Quest");
    }

    @Override
    public void execute(String[] args) {
    //print the quest list with to do tasks

    }

    @Override
    public String getDescription() {
        return "-shows the progress on the current quest";
    }
}
