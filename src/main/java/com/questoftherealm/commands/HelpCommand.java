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
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("help.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("help.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args,Player player, GameState state) {
        if(!makeSafe(args,player,state)){
            return;
        }
        state.getGameServices().getOutput().println(state.getMessages().getBundle().get("help.header"));
        factory.getAllCommands().entrySet().stream()
                .sorted((e1,e2)->(e1.getKey().compareToIgnoreCase(e2.getKey())))
                .forEach((entry) -> state.getGameServices().getOutput().println(state.getMessages().getBundle().get("help.commandFormat", entry.getKey(), entry.getValue().getDescription(state))));
    }
}