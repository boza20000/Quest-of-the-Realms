package com.questoftherealm.commands;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameState;

public class LookCommand extends Command {

    public LookCommand() {
        super("look");
    }

    @Override
    public String getDescription(GameState state) {
        return state.getMessages().getBundle().get("look.description");
    }

    @Override
    public boolean makeSafe(String[] args, Player player,GameState state) {
        if (args.length != 1) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("look.usage", getDescription(state)));
            return false;
        }
        return playerBaseCheck(player,state);
    }

    @Override
    public void execute(String[] args, Player player, GameState state) {
        if (!makeSafe(args, player,state)) {
            return;
        }
        try {
            state.getGameServices().getOutput().print(state.getMessages().getBundle().get("look.info.looking"));
            state.getGameServices().getOutput().flush();
            for (int i = 0; i < 3; i++) {
                if(!state.isSimulation()) {
                    Thread.sleep(1000);
                }
                state.getGameServices().getOutput().print(state.getMessages().getBundle().get("look.info.dot"));
                state.getGameServices().getOutput().flush();
            }
            player.look(state);
        } catch (Exception e) {
            state.getGameServices().getOutput().println(state.getMessages().getBundle().get("look.error.failed"));
        }
    }
}