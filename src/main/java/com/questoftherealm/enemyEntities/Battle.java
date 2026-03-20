package com.questoftherealm.enemyEntities;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.commands.Command;
import com.questoftherealm.commands.CommandFactory;
import com.questoftherealm.exceptions.InvalidCommand;
import com.questoftherealm.exceptions.ItemNotFound;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.interaction.SlowPrinter;

public class Battle {
    private final Player player;
    private final Enemy enemy;
    private final GameState state;
    private final Output output;
    private final CommandFactory commandFactory;
    private final SlowPrinter slowPrinter;

    private int comboCounter = 0;
    private int escapeAttempts = 0;
    private boolean playerBlocking = false;
    private boolean enemyHeavyAttack = false;

    public Battle(Player player, Enemy enemy, GameState state) {
        this.player = player;
        this.enemy = enemy;
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.commandFactory = new CommandFactory();
        this.slowPrinter = new SlowPrinter(state);
    }

    public boolean simulate() {
        slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.start", enemy.getType()));

        while (!player.getPlayerCharacter().isDead() && enemy.isAlive()) {
            printEnemyIntent();
            printIntro();
            String choice = state.getGameServices().getInput().nextLine();

            playerBlocking = false;

            switch (choice) {
                case "1" -> performAttack();
                case "2" -> performBlock();
                case "3" -> {
                    useItem();
                    comboCounter = 0;
                }
                case "4" -> {
                    if (attemptFlee()) {
                        return false;
                    }
                    comboCounter = 0;
                }
                default -> {
                    output.println(state.getMessages().getBundle().get("battle.invalidChoice"));
                    continue;
                }
            }

            // Enemy attacks after every player action (except flee success)
            if (enemy.isAlive() && !player.getPlayerCharacter().isDead()) {
                performEnemyAttack();
            }
        }

        return handleDead();
    }

    private void performAttack() {
        comboCounter++;
        Characters pc = player.getPlayerCharacter();

        if (comboCounter > 1) {
            output.println(state.getMessages().getBundle().get("battle.combo", comboCounter));
        }

        // Critical hit check: intelligence * 3%, capped at 30%
        double critChance = Math.min(pc.getIntelligence() * 0.03, 0.30);
        double critRoll = state.getGameServices().getRandom().randomDouble(1);

        if (critRoll < critChance) {
            // Critical hit!
            int critDamage = (int) (pc.getAttack() * 1.5);
            // Apply combo bonus: +10% per combo beyond 1
            critDamage += (int) (critDamage * (comboCounter - 1) * 0.10);
            output.println(state.getMessages().getBundle().get("battle.crit.player", critDamage));
            enemy.takeDamage(critDamage, state);
        } else {
            // Normal attack with combo bonus
            int damage = pc.getAttack();
            damage += (int) (damage * (comboCounter - 1) * 0.10);
            pc.attack(enemy, player, state);
            // If combo bonus applied extra damage beyond base, deal the extra
            int bonusDamage = damage - pc.getAttack();
            if (bonusDamage > 0 && enemy.isAlive()) {
                enemy.takeDamage(bonusDamage, state);
            }
        }
    }

    private void performBlock() {
        playerBlocking = true;
        comboCounter = 0;
        Characters pc = player.getPlayerCharacter();

        // Block effectiveness scales with defence: defence * 15%, capped at 75%
        double blockPercent = Math.min(pc.getDefence() * 0.15, 0.75);
        output.println(state.getMessages().getBundle().get("battle.block.brace",
                (int) (blockPercent * 100)));

        // If enemy is doing a heavy attack, blocking is extra rewarding
        if (enemyHeavyAttack) {
            output.println(state.getMessages().getBundle().get("battle.block.heavyReward"));
        }
    }

    private void useItem() {
        player.openInventory(state);
        output.println(state.getMessages().getBundle().get("battle.enterItem"));
        String[] useCommand = new String[]{state.getGameServices().getInput().nextLine()};
        while (true) {
            try {
                Command cmd = commandFactory.getCommand("use", state);
                cmd.execute(useCommand, player, state);
            } catch (InvalidCommand | ItemNotFound e) {
                output.println(state.getMessages().getBundle().get("battle.item.invalid"));
                continue;
            }
            break;
        }
    }

    private boolean attemptFlee() {
        escapeAttempts++;
        double roll = state.getGameServices().getRandom().randomDouble(1);
        // First attempt: 30% chance, second: 15%, after that: 0%
        double escapeChance = escapeAttempts <= 1 ? 0.30 : (escapeAttempts <= 2 ? 0.15 : 0.0);
        if (roll < escapeChance) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.escape.success"));
            return true;
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.escape.fail"));
            return false;
        }
    }

    private void printEnemyIntent() {
        // Decide if this will be a heavy attack (30% chance)
        double heavyRoll = state.getGameServices().getRandom().randomDouble(1);
        enemyHeavyAttack = heavyRoll < 0.30;

        if (enemyHeavyAttack) {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.telegraph.heavy", enemy.getType()));
        } else {
            String key = "battle.telegraph." + enemy.getType().name().toLowerCase();
            slowPrinter.slowPrint(state.getMessages().getBundle().get(key));
        }
    }

    private void performEnemyAttack() {
        Characters pc = player.getPlayerCharacter();

        // Player dodge check: defence * 2%, capped at 20%
        double dodgeChance = Math.min(pc.getDefence() * 0.02, 0.20);
        double dodgeRoll = state.getGameServices().getRandom().randomDouble(1);

        if (dodgeRoll < dodgeChance && !enemyHeavyAttack) {
            // Dodged! Heavy attacks can't be dodged
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.dodge"));
            return;
        }

        // Calculate enemy damage
        int baseDamage = enemy.getBaseAttack() + (enemy.getWeapon() != null ? enemy.getWeapon().getPower() : 0);

        // Heavy attack deals 1.8x damage
        if (enemyHeavyAttack) {
            baseDamage = (int) (baseDamage * 1.8);
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.enemy.heavy", enemy.getType(), baseDamage));
        }

        // Enemy critical hit: 10% flat chance
        double enemyCritRoll = state.getGameServices().getRandom().randomDouble(1);
        boolean enemyCrit = enemyCritRoll < 0.10;
        if (enemyCrit) {
            baseDamage = (int) (baseDamage * 1.5);
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.crit.enemy", enemy.getType(), baseDamage));
        }

        if (playerBlocking) {
            // Block reduces damage
            double blockPercent = Math.min(pc.getDefence() * 0.15, 0.75);
            // Blocking a heavy attack is extra effective
            if (enemyHeavyAttack) {
                blockPercent = Math.min(blockPercent + 0.15, 0.90);
            }
            int reducedDamage = (int) (baseDamage * (1.0 - blockPercent));
            output.println(state.getMessages().getBundle().get("battle.block.reduced",
                    (int) (blockPercent * 100), reducedDamage));
            if (reducedDamage > 0) {
                pc.takeDamage(reducedDamage, state, player);
            } else {
                output.println(state.getMessages().getBundle().get("battle.block.success"));
            }

            // Counter-attack opportunity on a good block (>50% reduction)
            if (blockPercent > 0.50 && enemy.isAlive() && !pc.isDead()) {
                int counterDamage = (int) (pc.getAttack() * 0.5);
                slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.counter", counterDamage));
                enemy.takeDamage(counterDamage, state);
            }
        } else {
            // Normal enemy attack
            if (!enemyCrit && !enemyHeavyAttack) {
                output.println(state.getMessages().getBundle().get("enemy.attack.player",
                        enemy.getClass().getSimpleName(), baseDamage));
            }
            pc.takeDamage(baseDamage, state, player);
        }
    }

    // --- UI ---

    private void printIntro() {
        output.println();
        output.println(state.getMessages().getBundle().get("battle.status",
                player.getPlayerCharacter().getHealth(),
                enemy.getHealth()));

        output.println(state.getMessages().getBundle().get("battle.chooseAction"));
    }

    // --- Outcome ---

    private boolean handleDead() {
        if (player.getPlayerCharacter().isDead()) {
            player.setDead();
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.player.defeated"));
            return false;
        } else {
            slowPrinter.slowPrint(state.getMessages().getBundle().get("battle.enemy.defeated", enemy.getType()));
            handleRewards();
            return true;
        }
    }

    private void handleRewards() {
        player.addExp(enemy.getXpReward());
        player.addMoney(enemy.getGoldReward(), state);
        output.println(state.getMessages().getBundle().get("battle.enemy.defeated.rewards", enemy.getXpReward(), enemy.getGoldReward()));
        for (Loot loot : enemy.getLoot()) {
            int quantity = state.getGameServices().getRandom().randomInt(loot.min(), loot.max());
            double roll = state.getGameServices().getRandom().randomDouble(1);
            if (roll <= loot.chance()) {
                output.println(state.getMessages().getBundle().get("battle.enemy.defeated.loot", enemy.getClass().getSimpleName(), loot.item(), quantity));
                player.getInventory().addItem(loot.item(), quantity, state);
            }
        }
    }
}
