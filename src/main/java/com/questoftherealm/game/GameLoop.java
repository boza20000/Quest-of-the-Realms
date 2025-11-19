package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.localization.MessageBundle;

public class GameLoop {
    private final CommandFactory factory = new CommandFactory();

    public void startLoop(Game game) {
        Player player = game.getGameState().getPlayer();
        MissionInteractions missionInteractions = new MissionInteractions(game.getGameState());
        missionInteractions.worldStart(player);
        player.setStartTime(System.currentTimeMillis());
        Output output = game.getGameState().getGameServices().getOutput();

        while (!game.getGameState().isGameOver()) {
            output.print(MessageBundle.get("console.enter.command.symbol"));
            String command = game.getGameState().getGameServices().getInput().nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            String[] parts = command.trim().split("\\s+");
            String commandName = parts[0];
            Command cmd = null;

            try {
                cmd = factory.getCommand(commandName);
            } catch (InvalidCommand e) {
                output.println(MessageBundle.get("error.command.InvalidCommand"));
            }

            if (cmd == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    output.println(MessageBundle.get("error.command.sleepFail"));
                }
            } else {
                try {
                    cmd.execute(parts, player, game.getGameState());
                    output.println(MessageBundle.get("gameLoop.command.success"));
                    player.updateQuestStatus(game.getGameState());
                } catch (Exception e) {
                    output.println(MessageBundle.get("gameLoop.command.syntax"));
                    output.print(cmd.getDescription());
                }
            }
        }

        player.trackPlayTime();
        game.getConsole().displayEnd(player);
    }
}
