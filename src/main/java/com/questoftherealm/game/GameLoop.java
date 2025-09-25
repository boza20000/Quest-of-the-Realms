package com.questoftherealm.game;

import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.interaction.Console;
import com.questoftherealm.interaction.Interactions;

import java.util.Scanner;

import static com.questoftherealm.game.Game.gameOver;

public class GameLoop {
    private final Scanner scanner = new Scanner(System.in);
    private final CommandFactory factory = new CommandFactory();

    public void startLoop() {
        Console.worldIntro();
        Interactions.worldStart();
        while (!gameOver) {
            System.out.print(">");
            String command = scanner.nextLine().trim();
            if (command.isEmpty()) {
                System.out.print("\033[1A\033[2K\r");
                continue;
            }
            String[] parts = command.trim().split("\\s+");
            String commandName = parts[0];
            Command cmd = null;
            try {
                cmd = factory.getCommand(commandName);
            } catch (InvalidCommand e) {
                System.out.println(e.getMessage());
            }
            if (cmd == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print("\033[1A\033[2K\r");
            } else {
                try {
                    cmd.execute(parts);
                    System.out.println("Command went trough");
                    for (Quest q : Game.getQuests()) {
                        q.updateStatus();
                        if (q.isCompleted()) {
                            Game.getQuests().poll();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Command syntax: ");
                    System.out.print(cmd.getDescription());
                }
            }
        }
    }
}
