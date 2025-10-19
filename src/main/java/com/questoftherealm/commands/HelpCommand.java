package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;

public class HelpCommand extends Command {
    private final CommandFactory factory;

    public HelpCommand(CommandFactory factory) {
        super("help");
        this.factory = factory;
    }

    @Override
    public String getDescription() {
        return "help — lists all available commands and their descriptions";
    }

    @Override
    public boolean makeSafe(String[] args, Player player) {
        if (args.length != 1) {
            System.out.println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player);
    }

    @Override
    public void execute(String[] args) {
        Player player = Game.getPlayer();
        if(!makeSafe(args,player)){
            return;
        }
        System.out.println("Available commands:");
        factory.getAllCommands().forEach((name, cmd) ->
                System.out.println("- " + name + "->" + cmd.getDescription())
        );
    }
}

