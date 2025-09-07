package com.questoftherealm.game.Commands;

public class AttackCommand extends Command {
    public AttackCommand(){
        super("attack");
    }

    @Override
    public void execute(String[] args) {

        String target = args[1];
    }

    @Override
    public String getDescription() {
        return "- attack [enemy name]";
    }
}
