package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.MessageBundle;

public class Battle {
    private Player player;
    private final Enemy enemy;
    private GameState state;
    private Output output;

    public Battle(Player player, Enemy enemy,GameState state) {
        this.player = player;
        this.enemy = enemy;
        this.state = state;
        this.output = state.getGameServices().getOutput();
    }

    public boolean simulate() {
        output.println(MessageBundle.get("battle.start", enemy.getType()));

        int escapeCount = 0;

        while (!player.getPlayerCharacter().isDead() && enemy.isAlive()) {
            output.println();
            output.println(MessageBundle.get("battle.status",
                    player.getPlayerCharacter().getHealth(),
                    enemy.getHealth()));

            output.println(MessageBundle.get("battle.chooseAction"));

            String choice = state.getGameServices().getInput().nextLine();

            switch (choice) {
                case "1" -> player.getPlayerCharacter().attack(enemy, player,state);

                case "2" -> {
                    player.openInventory(state);
                    output.println(MessageBundle.get("battle.enterItem"));
                    String[] useCommand = new String[]{state.getGameServices().getInput().nextLine()};
                    CommandFactory commandFactory = new CommandFactory();
                    Command cmd = commandFactory.getCommand("use");
                    cmd.execute(useCommand,player,state);
                }

                case "3" -> {
                    escapeCount++;
                    if (Math.random() < 0.5 && escapeCount <= 1) {
                        output.println(MessageBundle.get("battle.escape.success"));
                        return false;
                    } else {
                        output.println(MessageBundle.get("battle.escape.fail"));
                    }
                }

                default -> output.println(MessageBundle.get("battle.invalidChoice"));
            }

            if (enemy.isAlive()) {
                enemy.attack(player,state);
            }
        }

        if (player.getPlayerCharacter().isDead()) {
            output.println(MessageBundle.get("battle.player.defeated"));
            return false;
        } else {
            output.println(MessageBundle.get("battle.enemy.defeated", enemy.getType()));
            return true;
        }
    }
}
