package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;

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
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println("Usage: " + getDescription());
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args,Player player, GameState state) {
        if(!makeSafe(args,player,state)){
            return;
        }
        state.getGameServices().getOutput().println("Available commands:");
        factory.getAllCommands().entrySet().stream()
                .sorted((e1,e2)->(e1.getKey().compareToIgnoreCase(e2.getKey())))
                .forEach((entry) -> state.getGameServices().getOutput().println("- " + entry.getKey() + "->" + entry.getValue().getDescription()));
    }
}

