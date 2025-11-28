package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

public class Battle {
    private Player player;
    private final Enemy enemy;
    private GameState state;
    private Output output;
    private CommandFactory commandFactory ;

    public Battle(Player player, Enemy enemy,GameState state) {
        this.player = player;
        this.enemy = enemy;
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this. commandFactory = new CommandFactory();
    }

    public boolean simulate() {
        output.println(state.getMessages().getBundle().get("battle.start", enemy.getType()));

        int escapeCount = 0;

        while (!player.getPlayerCharacter().isDead() && enemy.isAlive()) {
            output.println();
            output.println(state.getMessages().getBundle().get("battle.status",
                    player.getPlayerCharacter().getHealth(),
                    enemy.getHealth()));

            output.println(state.getMessages().getBundle().get("battle.chooseAction"));

            String choice = state.getGameServices().getInput().nextLine();

            switch (choice) {
                case "1" -> player.getPlayerCharacter().attack(enemy, player,state);

                case "2" -> {
                    player.openInventory(state);
                    output.println(state.getMessages().getBundle().get("battle.enterItem"));
                    String[] useCommand = new String[]{state.getGameServices().getInput().nextLine()};
                    while(true) {
                        try {
                            Command cmd = commandFactory.getCommand("use",state);
                            cmd.execute(useCommand, player, state);
                        }
                        catch(Exception e){
                            continue;
                        }
                        break;
                    }
                }

                case "3" -> {
                    escapeCount++;
                    if (Math.random() < 0.5 && escapeCount <= 1) {
                        output.println(state.getMessages().getBundle().get("battle.escape.success"));
                        return false;
                    } else {
                        output.println(state.getMessages().getBundle().get("battle.escape.fail"));
                    }
                }

                default -> output.println(state.getMessages().getBundle().get("battle.invalidChoice"));
            }

            if (enemy.isAlive()) {
                enemy.attack(player,state);
            }
        }

        if (player.getPlayerCharacter().isDead()) {
            output.println(state.getMessages().getBundle().get("battle.player.defeated"));
            return false;
        } else {
            output.println(state.getMessages().getBundle().get("battle.enemy.defeated", enemy.getType()));
            return true;
        }
    }
}
