package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;

import java.util.Scanner;

public class Battle {
    private Player player;
    private Enemy enemy;
    private Scanner scanner = new Scanner(System.in);

    public Battle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public boolean simulate() {
        System.out.println("⚔️ A wild " + enemy.getType() + " appears!");
        int escapeCount = 0;
        while (!player.getPlayerCharacter().isDead() && enemy.isAlive()) {
            System.out.println("\nYour HP: " + player.getPlayerCharacter().getHealth() +
                    " | Enemy HP: " + enemy.getHealth());
            System.out.println("Choose action: (1) Attack  (2) Use Item  (3) Flee");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> player.getPlayerCharacter().attack(enemy, player);
                case "2" -> {
                    player.openInventory();
                    System.out.println("Enter item name to use:");
                    String[] useCommand = new String[]{scanner.nextLine()};
                    CommandFactory commandFactory = new CommandFactory();
                    Command cmd = commandFactory.getCommand("use");
                    cmd.execute(useCommand);
                }
                case "3" -> {
                    escapeCount++;
                    if (Math.random() < 0.5 && escapeCount <= 1) {
                        System.out.println("You escaped successfully!");
                        return false;
                    } else {
                        System.out.println("Escape failed!");
                    }
                }
                default -> System.out.println("Invalid choice.");
            }
            if (enemy.isAlive()) {
                enemy.attack(player);
            }
        }
        if (player.getPlayerCharacter().isDead()) {
            System.out.println("💀 You were defeated...");
            return false;
        } else {
            System.out.println("🏆 You defeated the " + enemy.getType() + "!");
            return true;
            //player.addMoney();
            // player.addExp();
        }
    }

}
