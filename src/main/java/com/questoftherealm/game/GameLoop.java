package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.AbilityException;
import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.exceptions.SaveError;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.server.ServerLogger;

public class GameLoop {
    private final CommandFactory factory;

    public GameLoop (){
        factory = new CommandFactory();
    }
    public GameLoop (CommandFactory factory){
        this.factory = factory;
    }

    public void startLoop(Game game, String username) {
        MissionInteractions m = new MissionInteractions(game.getGameState());
        Player curPlayer = game.getGameState().getPlayer(username);

        displayGameIntro(m, curPlayer);
        startClock(game, curPlayer);

        Output output = game.getGameState().getGameServices().getOutput();
        MessageBundle bundle = game.getGameState().getMessages().getBundle();

        while (!game.getGameState().isGameOver() && game.isRunning() && curPlayer.isActive()) {
            printSymbol(output, bundle);
            String command = game.getGameState().getGameServices().getInput().nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            String[] parts = command.trim().split("\\s+");
            String commandName = parts[0];

            processCommand(parts, commandName, output, game.getGameState(), bundle, curPlayer);

            if(curPlayer.isDead()){
                curPlayer.respawn(game.getGameState());
            }
        }

        endGame(game, curPlayer);
    }

    private void endGame(Game game, Player curPlayer) {
        curPlayer.trackPlayTime(game.getGameState());
      //  if(curPlayer.finishedGame()){
            game.getConsole().displayEnd(curPlayer);
      //  }

        game.getGameState().removePlayer(curPlayer.getName());
    }

    private void displayGameIntro(MissionInteractions m, Player player) {
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
        } catch (SaveError | AbilityException e) {
            output.println(e.getMessage());
        } catch (IllegalArgumentException e) {
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
            ServerLogger.get().warn("GameLoop: Game loop sleep interrupted", e);
            Thread.currentThread().interrupt();
            output.println(bundle.get("error.command.sleepFail"));
        }
    }

    private void printSymbol(Output output, MessageBundle bundle) {
        output.print(bundle.get("console.menu.prompt"));
        output.flush();
    }

}
