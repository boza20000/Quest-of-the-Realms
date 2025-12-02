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
        MissionInteractions m = new MissionInteractions(game.getGameState());
        displayGameIntro(m, game);
        startClock(game);

        Output output = game.getGameState().getGameServices().getOutput();
        MessageBundle bundle = game.getGameState().getMessages().getBundle();

        while (!game.getGameState().isGameOver() || game.getGameState().getPlayer().isDead()) {
            printSymbol(output, bundle);
            String command = game.getGameState().getGameServices().getInput().nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            String[] parts = command.trim().split("\\s+");
            String commandName = parts[0];
            processCommand(parts, commandName, output, game.getGameState(), bundle, game.getGameState().getPlayer());

        }

        endGame(game);

    }

    private void endGame(Game game) {
        game.getGameState().getPlayer().trackPlayTime(game.getGameState());
        game.getConsole().displayEnd(game.getGameState().getPlayer());
    }

    private void displayGameIntro(MissionInteractions m, Game game) {
        m.worldStart(game.getGameState().getPlayer());
    }

    private void startClock(Game game) {
        game.getGameState().getPlayer().setStartTime(game.getGameState().getClock().now());
    }


    private void processCommand(String[] parts, String commandName, Output output, GameState state, MessageBundle bundle, Player player) {
        Command cmd = createCommand(commandName, state, output, bundle);

        if (cmd == null) {
            sleep(output, bundle);
        } else {
            executeCommand(parts, cmd, output, state, bundle, player);
        }
    }

    private void executeCommand(String[] parts, Command cmd, Output output, GameState state, MessageBundle bundle, Player player) {
        try {
            cmd.execute(parts, player, state);
            output.println(bundle.get("gameLoop.command.success"));
            player.updateQuestStatus(state);
        } catch (Exception e) {
            output.println(bundle.get("gameLoop.command.syntax"));
            output.print(cmd.getDescription(state));
        }
    }

    private Command createCommand(String commandName, GameState state, Output output, MessageBundle bundle) {
        try {
            return factory.getCommand(commandName, state);
        } catch (InvalidCommand e) {
            output.println(bundle.get("error.command.InvalidCommand"));
            return null;
        }
    }

    private void sleep(Output output, MessageBundle bundle) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            output.println(bundle.get("error.command.sleepFail"));
        }
    }

    private void printSymbol(Output output, MessageBundle bundle) {
        output.print(bundle.get("console.enter.command.symbol"));
    }

}
