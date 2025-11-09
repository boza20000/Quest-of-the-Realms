package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.localization.MessageBundle;

import java.util.Scanner;

public class Battle {
    private final Player player;
    private final Enemy enemy;
    private final Scanner scanner = new Scanner(System.in);

    public Battle(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public boolean simulate() {
        System.out.println(MessageBundle.get("battle.start", enemy.getType()));

        int escapeCount = 0;

        while (!player.getPlayerCharacter().isDead() && enemy.isAlive()) {
            System.out.println();
            System.out.println(MessageBundle.get("battle.status",
                    player.getPlayerCharacter().getHealth(),
                    enemy.getHealth()));

            System.out.println(MessageBundle.get("battle.chooseAction"));

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> player.getPlayerCharacter().attack(enemy, player);

                case "2" -> {
                    player.openInventory();
                    System.out.println(MessageBundle.get("battle.enterItem"));
                    String[] useCommand = new String[]{scanner.nextLine()};
                    CommandFactory commandFactory = new CommandFactory();
                    Command cmd = commandFactory.getCommand("use");
                    cmd.execute(useCommand);
                }

                case "3" -> {
                    escapeCount++;
                    if (Math.random() < 0.5 && escapeCount <= 1) {
                        System.out.println(MessageBundle.get("battle.escape.success"));
                        return false;
                    } else {
                        System.out.println(MessageBundle.get("battle.escape.fail"));
                    }
                }

                default -> System.out.println(MessageBundle.get("battle.invalidChoice"));
            }

            if (enemy.isAlive()) {
                enemy.attack(player);
            }
        }

        if (player.getPlayerCharacter().isDead()) {
            System.out.println(MessageBundle.get("battle.player.defeated"));
            return false;
        } else {
            System.out.println(MessageBundle.get("battle.enemy.defeated", enemy.getType()));
            return true;
        }
    }
}
