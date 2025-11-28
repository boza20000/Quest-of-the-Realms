package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;

public class GameLoop {
    private final CommandFactory factory = new CommandFactory();

    public void startLoop(Game game) {

        MissionInteractions missionInteractions = new MissionInteractions(game.getGameState());
        missionInteractions.worldStart( game.getGameState().getPlayer());
        game.getGameState().getPlayer().setStartTime(game.getGameState().getClock().now());
        Output output = game.getGameState().getGameServices().getOutput();

        while (!game.getGameState().isGameOver() || game.getGameState().getPlayer().isDead()) {
            output.print(game.getGameState().getMessages().getBundle().get("console.enter.command.symbol"));
            String command = game.getGameState().getGameServices().getInput().nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            String[] parts = command.trim().split("\\s+");
            String commandName = parts[0];
            Command cmd = null;

            try {
                cmd = factory.getCommand(commandName,game.getGameState());
            } catch (InvalidCommand e) {
                output.println(game.getGameState().getMessages().getBundle().get("error.command.InvalidCommand"));
            }

            if (cmd == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    output.println(game.getGameState().getMessages().getBundle().get("error.command.sleepFail"));
                }
            } else {
                try {
                    cmd.execute(parts, game.getGameState().getPlayer(), game.getGameState());
                    output.println(game.getGameState().getMessages().getBundle().get("gameLoop.command.success"));
                    game.getGameState().getPlayer().updateQuestStatus(game.getGameState());
                } catch (Exception e) {
                    output.println(game.getGameState().getMessages().getBundle().get("gameLoop.command.syntax"));
                    output.print(cmd.getDescription(game.getGameState()));
                }
            }
        }

        game.getGameState().getPlayer().trackPlayTime(game.getGameState());
        game.getConsole().displayEnd( game.getGameState().getPlayer());
    }
}
