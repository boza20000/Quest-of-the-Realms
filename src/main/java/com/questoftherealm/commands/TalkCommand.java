package com.questoftherealm.commands;

public class TalkCommand extends Command{
    public TalkCommand(){
        super("talk");
    }

    @Override
    public void execute(String[] args) {
        String target = args[1];


    }

    @Override
    public String getDescription() {
        return "you talk with the NPC in order to trade or exchange information";
    }
}
