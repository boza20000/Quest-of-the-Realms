package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinKing;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;

import java.util.Random;
import java.util.Scanner;

public class GoblinKingManager {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public void goblinKingdomFound(Player player, FinalBattle q) {
        SlowPrinter.slowPrint("🌄 " + MessageBundle.get("goblinking.intro"));
        q.setBreached(true);

        System.out.println(MessageBundle.get("goblinking.choice.menu"));
        int choice;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            choice = random.nextInt(1, 3);
        }
        if (choice == 1) {
            stealthInfiltration(q);
        } else {
            fullAssault(player, q);
        }
    }

    private void fullAssault(Player player, FinalBattle q) {
        SlowPrinter.slowPrint("⚔️ " + MessageBundle.get("goblinking.assault.start"));
        SlowPrinter.slowPrint("💥 " + MessageBundle.get("goblinking.assault.mid"));
        System.out.println("🔥 " + MessageBundle.get("goblinking.assault.fight"));
        startBossFight(player, q);
    }

    private void stealthInfiltration(FinalBattle q) {
        SlowPrinter.slowPrint("🕶️ " + MessageBundle.get("goblinking.stealth.start"));
        pause();

        SlowPrinter.slowPrint("💀 " + MessageBundle.get("goblinking.stealth.throne"));

        int subChoice;
        try {
            subChoice = scanner.nextInt();
        } catch (Exception e) {
            subChoice = random.nextInt(1, 3);
        }

        if (subChoice == 1) {
            SlowPrinter.slowPrint("⚡ " + MessageBundle.get("goblinking.stealth.attack"));
            q.setDefeated(true);
            SlowPrinter.slowPrint("👑 " + MessageBundle.get("goblinking.stealth.success"));
        } else {
            SlowPrinter.slowPrint("⏳ " + MessageBundle.get("goblinking.stealth.wait"));
            q.setDefeated(true);
            SlowPrinter.slowPrint("✨ " + MessageBundle.get("goblinking.stealth.silent.success"));
        }
    }

    private void startBossFight(Player player, FinalBattle q) {
        Characters character = player.getPlayerCharacter();
        GoblinKing king = new GoblinKing();
        int round = 1;

        SlowPrinter.slowPrint("👑 " + MessageBundle.get("goblinking.boss.intro"));

        while (!character.isDead() && !king.isDead()) {
            System.out.println("\n🔥 ROUND " + round++ + " 🔥");
            System.out.println("💚 " + MessageBundle.get("goblinking.status.player", character.getHealth(), character.getMana()));
            System.out.println("❤️ " + MessageBundle.get("goblinking.status.boss", king.getHealth()));
            SlowPrinter.slowPrint("\n" + king.getName() + " " + MessageBundle.get("goblinking.boss.prepare"));

            int bossMove = random.nextInt(5);
            switch (bossMove) {
                case 0 -> System.out.println("⚔️ " + MessageBundle.get("goblinking.boss.move.0"));
                case 1 -> System.out.println("🏃‍♂️ " + MessageBundle.get("goblinking.boss.move.1"));
                case 2 -> System.out.println("😈 " + MessageBundle.get("goblinking.boss.move.2"));
                case 3 -> System.out.println("💨 " + MessageBundle.get("goblinking.boss.move.3"));
                case 4 -> System.out.println("🌋 " + MessageBundle.get("goblinking.boss.move.4"));
            }

            System.out.println(MessageBundle.get("goblinking.player.menu"));
            System.out.print("> ");
            String input = scanner.nextLine();

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
                        if (random.nextInt(100) < 60) {
                            System.out.println("💨 " + MessageBundle.get("goblinking.player.dodge.success"));
                        } else {
                            System.out.println("❌ " + MessageBundle.get("goblinking.player.dodge.fail"));
                            damageToPlayer = king.getBaseAttack() / 2;
                        }
                    } else {
                        System.out.println("⚠️ " + MessageBundle.get("goblinking.player.nomana"));
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 2 -> { // Block
                    System.out.println("🛡️ " + MessageBundle.get("goblinking.player.block"));
                    damageToPlayer = king.getBaseAttack() / 3;
                    if (random.nextInt(100) < 30) {
                        System.out.println("💥 " + MessageBundle.get("goblinking.player.parry"));
                        damageToBoss = character.getAttack() / 2;
                    }
                }
                case 3 -> { // Counterattack
                    System.out.println("⚔️ " + MessageBundle.get("goblinking.player.counter.start"));
                    if (random.nextInt(100) < 45) {
                        System.out.println("🔥 " + MessageBundle.get("goblinking.player.counter.success"));
                        damageToBoss = character.getAttack();
                    } else {
                        System.out.println("💀 " + MessageBundle.get("goblinking.player.counter.fail"));
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 4 -> { // Hide
                    System.out.println("🏗️ " + MessageBundle.get("goblinking.player.hide"));
                    if (random.nextInt(100) < 60) {
                        System.out.println("🎯 " + MessageBundle.get("goblinking.player.hide.success"));
                        damageToBoss = (int) (character.getAttack() * 1.3);
                    } else {
                        System.out.println("💀 " + MessageBundle.get("goblinking.player.hide.fail"));
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                case 5 -> { // Potion
                    try {
                        String item = scanner.nextLine();
                        Item i = ItemRegistry.getItem(item);
                        player.useItem(i);
                    } catch (Exception e) {
                        System.out.println("⚠️ " + MessageBundle.get("goblinking.player.itemfail"));
                    }
                    System.out.println("💥 " + MessageBundle.get("goblinking.player.attack"));
                    if (random.nextInt(100) < 50) {
                        damageToBoss = (int) (character.getAttack() * 1.5);
                        System.out.println("🔥 " + MessageBundle.get("goblinking.player.attack.success"));
                    } else {
                        System.out.println("😖 " + MessageBundle.get("goblinking.player.attack.fail"));
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                default -> {
                    System.out.println("😨 " + MessageBundle.get("goblinking.player.hesitate"));
                    damageToPlayer = king.getBaseAttack();
                }
            }

            if (bossMove == 4 && random.nextInt(100) < 60) {
                System.out.println("🌪️ " + MessageBundle.get("goblinking.boss.super"));
                damageToPlayer += (int) (king.getBaseAttack() * 1.5);
            }

            if ((double) king.getHealth() / CharacterConstants.GoblinKing_HEALTH <= 0.2 && random.nextInt(100) < 40) {
                System.out.println("💢 " + MessageBundle.get("goblinking.boss.enrage"));
                king.setAttack((int) (king.getBaseAttack() * 1.3));
            }

            if (damageToBoss > 0) {
                king.takeDamage(damageToBoss);
                System.out.println("💥 " + MessageBundle.get("goblinking.damage.boss", damageToBoss));
            }
            if (damageToPlayer > 0) {
                character.takeDamage(damageToPlayer);
                System.out.println("😖 " + MessageBundle.get("goblinking.damage.player", damageToPlayer));
            }

            if (!king.isDead() && !character.isDead()) pause();
        }

        if (king.isDead()) {
            SlowPrinter.slowPrint("⚔️ " + MessageBundle.get("goblinking.boss.death"));
            q.setDefeated(true);
        } else {
            SlowPrinter.slowPrint("💀 " + MessageBundle.get("goblinking.boss.playerdeath"));
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
