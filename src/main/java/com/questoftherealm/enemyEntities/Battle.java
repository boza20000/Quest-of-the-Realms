package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.map.Tile;

public class Battle {
    private Player player;
    private final Enemy enemy;
    private GameState state;
    private Output output;
    private CommandFactory commandFactory;

    public Battle(Player player, Enemy enemy, GameState state) {
        this.player = player;
        this.enemy = enemy;
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.commandFactory = new CommandFactory();
    }

    public boolean simulate() {
        output.println(state.getMessages().getBundle().get("battle.start", enemy.getType()));
        int escapeCount = 0;

        while (!player.getPlayerCharacter().isDead() && enemy.isAlive()) {
            printIntro();
            String choice = state.getGameServices().getInput().nextLine();

            switch (choice) {
                case "1" -> player.getPlayerCharacter().attack(enemy, player, state);
                case "2" -> Choice2();
                case "3" -> {
                    if(Choice3(escapeCount)){
                        return false;
                    }

                }

                default -> output.println(state.getMessages().getBundle().get("battle.invalidChoice"));
            }

            if (enemy.isAlive()) {
                enemy.attack(player, state);
            }
        }

        return handleDead();
    }

    private void printIntro() {
        output.println();
        output.println(state.getMessages().getBundle().get("battle.status",
                player.getPlayerCharacter().getHealth(),
                enemy.getHealth()));

        output.println(state.getMessages().getBundle().get("battle.chooseAction"));

    }

    private void Choice2() {
        player.openInventory(state);
        output.println(state.getMessages().getBundle().get("battle.enterItem"));
        String[] useCommand = new String[]{state.getGameServices().getInput().nextLine()};
        while (true) {
            try {
                Command cmd = commandFactory.getCommand("use", state);
                cmd.execute(useCommand, player, state);
            } catch (Exception e) {
                continue;
            }
            break;
        }
    }

    private boolean Choice3(int escapeCount) {
        escapeCount++;
        double roll = state.getGameServices().getRandom().randomDouble(1);
        if (roll < 0.4 && escapeCount <= 1) {
            output.println(state.getMessages().getBundle().get("battle.escape.success"));
            return true;
        } else {
            output.println(state.getMessages().getBundle().get("battle.escape.fail"));
        }
        return false;
    }

    private boolean handleDead() {
        if (player.getPlayerCharacter().isDead()) {
            output.println(state.getMessages().getBundle().get("battle.player.defeated"));
            return false;
        } else {
            output.println(state.getMessages().getBundle().get("battle.enemy.defeated", enemy.getType()));
            Tile curTile = state.getMap().curZone(player.getX(),player.getY());
            if(curTile!=null){
                curTile.addEnemyLoot(enemy,state);
            }
            return true;
        }
    }
}
