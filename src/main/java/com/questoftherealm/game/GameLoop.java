package com.questoftherealm.game;

import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.expeditions.Quest;
import com.questoftherealm.game.Commands.Command;
import com.questoftherealm.game.Commands.CommandFactory;

import java.util.Scanner;

import static com.questoftherealm.game.Game.gameOver;

public class GameLoop {
    private final Scanner scanner = new Scanner(System.in);
    private final CommandFactory factory = new CommandFactory();

    public void startLoop() {
        worldIntro();
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
                        if(q.isCompleted()){
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

    private void worldIntro() {
        System.out.println("🌍 And so, your journey begins...");
        System.out.println("You stand at the gates of the king’s castle, the weight of destiny upon your shoulders.");
        System.out.println("The banners of the four kingdoms ripple in the wind, their fate resting in your hands.");
        System.out.println("Whispers of a shadowy enemy spread across the land—an unknown force threatening to consume all.");
        System.out.println("\n(Press ENTER to continue...)");
        scanner.nextLine();
        System.out.println("⚔️ The king has entrusted you with a sacred mission:");
        System.out.println("Unite the realms. Face the darkness. Become the hero this world desperately needs.");
        System.out.println("Your legend starts now...");
    }


}
