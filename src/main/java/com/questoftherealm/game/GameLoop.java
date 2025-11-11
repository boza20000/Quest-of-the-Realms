package com.questoftherealm.game;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.interaction.Console;
import com.questoftherealm.interaction.MissionInteractions;
import com.questoftherealm.localization.MessageBundle;

import java.util.Scanner;

import static com.questoftherealm.game.Game.gameOver;

public class GameLoop {
    private final Scanner scanner = new Scanner(System.in);
    private final CommandFactory factory = new CommandFactory();
    private final Console console = new Console();

    public void startLoop() {
        MissionInteractions.worldStart();
        Player player = Game.getPlayer();
        player.setStartTime(System.currentTimeMillis());

        while (!gameOver) {
            System.out.print(MessageBundle.get("console.enter.command.symbol"));
            String command = scanner.nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            String[] parts = command.trim().split("\\s+");
            String commandName = parts[0];
            Command cmd = null;

            try {
                cmd = factory.getCommand(commandName);
            } catch (InvalidCommand e) {
                System.out.println(MessageBundle.get("error.command.InvalidCommand"));
            }

            if (cmd == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(MessageBundle.get("error.command.sleepFail"));
                }
            } else {
                try {
                    cmd.execute(parts);
                    System.out.println(MessageBundle.get("gameLoop.command.success"));
                    player.updateQuestStatus();
                } catch (Exception e) {
                    System.out.println(MessageBundle.get("gameLoop.command.syntax"));
                    System.out.print(cmd.getDescription());
                }
            }
        }

        player.trackPlayTime();
        console.displayEnd(player);
    }
}
