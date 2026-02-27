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

    public void startLoop(Game game, String username) {
        MissionInteractions m = new MissionInteractions(game.getGameState());
        Player curPlayer = game.getGameState().getPlayer(username);

        displayGameIntro(m, game, curPlayer);
        startClock(game, curPlayer);

        Output output = game.getGameState().getGameServices().getOutput();
        MessageBundle bundle = game.getGameState().getMessages().getBundle();

        while (!game.getGameState().isGameOver()) {
            printSymbol(output, bundle);
            String command = game.getGameState().getGameServices().getInput().nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            String[] parts = command.trim().split("\\s+");
            String commandName = parts[0];
            processCommand(parts, commandName, output, game.getGameState(), bundle, curPlayer);

            //process map changes

        }

        endGame(game);
    }

    private void endGame(Game game) {
        for (Player player : game.getGameState().getActivePlayers()) {
            player.trackPlayTime(game.getGameState());
            game.getConsole().displayEnd(player);
        }
    }

    private void displayGameIntro(MissionInteractions m, Game game, Player player) {
        m.worldStart(player);
    }

    private void startClock(Game game, Player player) {
        player.setStartTime(game.getGameState().getClock().now());
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
            output.flush();
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
      output.print(bundle.get("console.menu.prompt"));
        output.flush();
    }

}
