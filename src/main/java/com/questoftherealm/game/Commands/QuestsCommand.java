package com.questoftherealm.game.Commands;

public class QuestsCommand extends Command {

    public QuestsCommand() {
        super("quests");
    }

    @Override
    public String getDescription() {
        return "gives a list of all current quests";
    }

    @Override
    public void execute(String[] args) {

    }

}
