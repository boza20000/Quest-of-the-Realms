package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinKing;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;


public class GoblinKingManager {
    private final Output output;
    private final GameState state;
    private final SlowPrinter slowPrinter;
    private final ItemRegistry itemRegistry;

    public GoblinKingManager(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.slowPrinter = new SlowPrinter(state);
        itemRegistry = state.getItemRegistry();
    }

    public void goblinKingdomFound(Player player, FinalBattle q) {
        slowPrinter.slowPrint("🌄 " + state.getMessages().getBundle().get("goblinking.intro"));
        q.setBreached(true);

        output.println(state.getMessages().getBundle().get("goblinking.choice.menu"));
        int choice;
        try {
            choice = state.getGameServices().getInput().nextInt();
        } catch (Exception e) {
            choice = state.getGameServices().getRandom().random().nextInt(1, 3);
        }
        if (choice == 1) {
            stealthInfiltration(q);
        } else {
            fullAssault(player, q);
        }
    }

    private void fullAssault(Player player, FinalBattle q) {
        slowPrinter.slowPrint("⚔️ " + state.getMessages().getBundle().get("goblinking.assault.start"));
        slowPrinter.slowPrint("💥 " + state.getMessages().getBundle().get("goblinking.assault.mid"));
        output.println("🔥 " + state.getMessages().getBundle().get("goblinking.assault.fight"));
        startBossFight(player, q);
    }

    private void stealthInfiltration(FinalBattle q) {
        slowPrinter.slowPrint("🕶️ " + state.getMessages().getBundle().get("goblinking.stealth.start"));
        pause();

        slowPrinter.slowPrint("💀 " + state.getMessages().getBundle().get("goblinking.stealth.throne"));

        int subChoice;
        try {
            subChoice = state.getGameServices().getInput().nextInt();
        } catch (Exception e) {
            subChoice = state.getGameServices().getRandom().random().nextInt(1, 3);
        }

        if (subChoice == 1) {
            slowPrinter.slowPrint("⚡ " + state.getMessages().getBundle().get("goblinking.stealth.attack"));
            q.setDefeated(true);
            slowPrinter.slowPrint("👑 " + state.getMessages().getBundle().get("goblinking.stealth.success"));
        } else {
            slowPrinter.slowPrint("⏳ " + state.getMessages().getBundle().get("goblinking.stealth.wait"));
            q.setDefeated(true);
            slowPrinter.slowPrint("✨ " + state.getMessages().getBundle().get("goblinking.stealth.silent.success"));
        }
    }

    private void startBossFight(Player player, FinalBattle q) {
        Characters character = player.getPlayerCharacter();
        GoblinKing king = new GoblinKing(state);
        int round = 1;

        slowPrinter.slowPrint("👑 " + state.getMessages().getBundle().get("goblinking.boss.intro"));

        while (!character.isDead() && !king.isDead()) {
            output.println("\n🔥 ROUND " + round++ + " 🔥");
            output.println("💚 " + state.getMessages().getBundle().get("goblinking.status.player", character.getHealth(), character.getMana()));
            output.println("❤️ " + state.getMessages().getBundle().get("goblinking.status.boss", king.getHealth()));
            slowPrinter.slowPrint("\n" + king.getName() + " " + state.getMessages().getBundle().get("goblinking.boss.prepare"));

            int bossMove = state.getGameServices().getRandom().randomInt(5);
            switch (bossMove) {
                case 0 -> output.println("⚔️ " + state.getMessages().getBundle().get("goblinking.boss.move.0"));
                case 1 -> output.println("🏃‍♂️ " + state.getMessages().getBundle().get("goblinking.boss.move.1"));
                case 2 -> output.println("😈 " + state.getMessages().getBundle().get("goblinking.boss.move.2"));
                case 3 -> output.println("💨 " + state.getMessages().getBundle().get("goblinking.boss.move.3"));
                case 4 -> output.println("🌋 " + state.getMessages().getBundle().get("goblinking.boss.move.4"));
            }

            output.println(state.getMessages().getBundle().get("goblinking.player.menu"));
            output.print("> ");
            String input = state.getGameServices().getInput().nextLine();

            int damageToBoss = 0;
            int damageToPlayer = 0;
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {
                case 1 -> { // Dodge
                    if (character.getMana() >= 5) {
                        character.setMana(character.getMana() - 5);
                        if (state.getGameServices().getRandom().random().nextInt(100) < 60) {
                            output.println("💨 " + state.getMessages().getBundle().get("goblinking.player.dodge.success"));
                        } else {
                            output.println("❌ " + state.getMessages().getBundle().get("goblinking.player.dodge.fail"));
                            damageToPlayer = king.getBaseAttack() / 2;
                        }
                    } else {
                        output.println("⚠️ " + state.getMessages().getBundle().get("goblinking.player.nomana"));
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 2 -> { // Block
                    output.println("🛡️ " + state.getMessages().getBundle().get("goblinking.player.block"));
                    damageToPlayer = king.getBaseAttack() / 3;
                    if (state.getGameServices().getRandom().random().nextInt(100) < 30) {
                        output.println("💥 " + state.getMessages().getBundle().get("goblinking.player.parry"));
                        damageToBoss = character.getAttack() / 2;
                    }
                }
                case 3 -> { // Counterattack
                    output.println("⚔️ " + state.getMessages().getBundle().get("goblinking.player.counter.start"));
                    if (state.getGameServices().getRandom().random().nextInt(100) < 45) {
                        output.println("🔥 " + state.getMessages().getBundle().get("goblinking.player.counter.success"));
                        damageToBoss = character.getAttack();
                    } else {
                        output.println("💀 " + state.getMessages().getBundle().get("goblinking.player.counter.fail"));
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 4 -> { // Hide
                    output.println("🏗️ " + state.getMessages().getBundle().get("goblinking.player.hide"));
                    if (state.getGameServices().getRandom().random().nextInt(100) < 60) {
                        output.println("🎯 " + state.getMessages().getBundle().get("goblinking.player.hide.success"));
                        damageToBoss = (int) (character.getAttack() * 1.3);
                    } else {
                        output.println("💀 " + state.getMessages().getBundle().get("goblinking.player.hide.fail"));
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                case 5 -> { // Potion
                    try {
                        String item = state.getGameServices().getInput().nextLine();
                        Item i = itemRegistry.getItem(item);
                        player.useItem(i);
                    } catch (Exception e) {
                        output.println("⚠️ " + state.getMessages().getBundle().get("goblinking.player.itemfail"));
                    }
                    output.println("💥 " + state.getMessages().getBundle().get("goblinking.player.attack"));
                    if (state.getGameServices().getRandom().random().nextInt(100) < 50) {
                        damageToBoss = (int) (character.getAttack() * 1.5);
                        output.println("🔥 " + state.getMessages().getBundle().get("goblinking.player.attack.success"));
                    } else {
                        output.println("😖 " + state.getMessages().getBundle().get("goblinking.player.attack.fail"));
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                default -> {
                    output.println("😨 " + state.getMessages().getBundle().get("goblinking.player.hesitate"));
                    damageToPlayer = king.getBaseAttack();
                }
            }

            if (bossMove == 4 && state.getGameServices().getRandom().random().nextInt(100) < 60) {
                output.println("🌪️ " + state.getMessages().getBundle().get("goblinking.boss.super"));
                damageToPlayer += (int) (king.getBaseAttack() * 1.5);
            }

            if ((double) king.getHealth() / CharacterConstants.GoblinKing_HEALTH <= 0.2 && state.getGameServices().getRandom().random().nextInt(100) < 40) {
                output.println("💢 " + state.getMessages().getBundle().get("goblinking.boss.enrage"));
                king.setAttack((int) (king.getBaseAttack() * 1.3));
            }

            if (damageToBoss > 0) {
                king.takeDamage(damageToBoss, state);
                output.println("💥 " + state.getMessages().getBundle().get("goblinking.damage.boss", damageToBoss));
            }
            if (damageToPlayer > 0) {
                character.takeDamage(damageToPlayer, state);
                output.println("😖 " + state.getMessages().getBundle().get("goblinking.damage.player", damageToPlayer));
            }

            if (!king.isDead() && !character.isDead()) pause();
        }

        if (king.isDead()) {
            slowPrinter.slowPrint("⚔️ " + state.getMessages().getBundle().get("goblinking.boss.death"));
            q.setDefeated(true);
        } else {
            slowPrinter.slowPrint("💀 " + state.getMessages().getBundle().get("goblinking.boss.playerdeath"));
            character.setHealth(0);
        }
    }

    private void pause() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {
        }
    }
}
