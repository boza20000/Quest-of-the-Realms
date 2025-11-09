package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinGeneral;
import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_General;
import com.questoftherealm.game.Game;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GoblinGeneralManger {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public static void startFinalBattle() {
        System.out.println("\n⚔️ " + messages.getString("goblinGeneral.battle.start"));
        System.out.println(messages.getString("goblinGeneral.battle.march"));

        int playerArmyPower = calculatePlayerArmyPower();
        int enemyArmyPower = 120 + random.nextInt(60);

        System.out.println("🏇 " + messages.getString("goblinGeneral.battle.player.power") + playerArmyPower);
        System.out.println("👹 " + messages.getString("goblinGeneral.battle.enemy.power") + enemyArmyPower);
        System.out.println(messages.getString("goblinGeneral.battle.begin"));
        simulateArmyBattle(playerArmyPower, enemyArmyPower);
    }

    private static int calculatePlayerArmyPower() {
        int power = Assemble_an_Army.armyPower;
        power += Game.getPlayer().getLevel() * 5;
        return power;
    }

    private static void simulateArmyBattle(int playerPower, int enemyPower) {
        double winChance = (double) playerPower / (playerPower + enemyPower);
        int roll = random.nextInt(100);

        System.out.println("\n💥 " + messages.getString("goblinGeneral.battle.rage"));
        System.out.println(messages.getString("goblinGeneral.battle.scene"));

        if (Assemble_an_Army.knightsRecruited) {
            pause();
            System.out.println(messages.getString("goblinGeneral.battle.knights"));
        }
        if (Assemble_an_Army.magesRecruited) {
            pause();
            System.out.println(messages.getString("goblinGeneral.battle.mages"));
        }
        if (Assemble_an_Army.archersRecruited) {
            pause();
            System.out.println(messages.getString("goblinGeneral.battle.archers"));
        }

        pause();
        if (roll < (winChance * 100)) {
            System.out.println("\n🏆 " + messages.getString("goblinGeneral.battle.victory"));
            System.out.println(messages.getString("goblinGeneral.battle.general.appears"));
            duelGoblinGeneral();
        } else {
            System.out.println("\n💀 " + messages.getString("goblinGeneral.battle.defeat"));
            Game.getPlayer().getPlayerCharacter().setHealth(0);
            System.out.println(messages.getString("goblinGeneral.battle.death"));
        }
    }

    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            System.out.println(messages.getString("goblinGeneral.error.sleep"));
        }
    }

    private static void duelGoblinGeneral() {
        Player player = Game.getPlayer();
        goblinGeneralFight(player);
        if (!player.getPlayerCharacter().isDead()) {
            System.out.println("\n🔥 " + messages.getString("goblinGeneral.duel.victory"));
            Defeat_the_Goblin_General.isDefeated = true;
            System.out.println("🎉 " + messages.getString("goblinGeneral.duel.safe"));
        } else {
            System.out.println("\n💀 " + messages.getString("goblinGeneral.duel.death"));
            Game.getPlayer().getPlayerCharacter().setHealth(0);
            System.out.println(messages.getString("goblinGeneral.duel.defeated"));
        }
    }

    private static void goblinGeneralFight(Player player) {
        GoblinGeneral general = new GoblinGeneral();
        Characters character = player.getPlayerCharacter();
        System.out.println("\n👹 " + messages.getString("goblinGeneral.duel.intro"));
        int round = 1;

        while (!character.isDead() && !general.isDead()) {
            System.out.println("\n⚔️ " + messages.getString("goblinGeneral.duel.round") + " " + round++);
            System.out.println("💚 " + messages.getString("goblinGeneral.duel.hp") + character.getHealth() +
                    " | 🔵 " + messages.getString("goblinGeneral.duel.mana") + character.getMana());
            System.out.println("❤️ " + messages.getString("goblinGeneral.duel.boss.hp") + general.getHealth());
            SlowPrinter.slowPrint("\n" + general.getName() + messages.getString("goblinGeneral.duel.raise.weapon"));

            int bossMove = random.nextInt(3);
            switch (bossMove) {
                case 0 -> System.out.println("⚔️ " + messages.getString("goblinGeneral.duel.boss.swing"));
                case 1 -> System.out.println("🏃‍♂️ " + messages.getString("goblinGeneral.duel.boss.charge"));
                case 2 -> System.out.println("😈 " + messages.getString("goblinGeneral.duel.boss.feint"));
            }

            System.out.println("\n" + messages.getString("goblinGeneral.duel.action"));
            System.out.println("1️⃣ " + messages.getString("goblinGeneral.duel.option.dodge"));
            System.out.println("2️⃣ " + messages.getString("goblinGeneral.duel.option.block"));
            System.out.println("3️⃣ " + messages.getString("goblinGeneral.duel.option.counter"));
            System.out.print(messages.getString("goblinGeneral.duel.choose"));
            String input = scanner.nextLine();

            int damageToBoss = 0;
            int damageToPlayer = 0;

            switch (input) {
                case "1" -> {
                    if (character.getMana() >= 5) {
                        character.setMana(character.getMana() - 5);
                        if (random.nextInt(100) < 60)
                            System.out.println("💨 " + messages.getString("goblinGeneral.duel.dodge.success"));
                        else {
                            System.out.println("❌ " + messages.getString("goblinGeneral.duel.dodge.fail"));
                            damageToPlayer = general.getBaseAttack() / 2;
                        }
                    } else {
                        System.out.println("⚠️ " + messages.getString("goblinGeneral.duel.dodge.nomana"));
                        damageToPlayer = general.getBaseAttack();
                    }
                }
                case "2" -> {
                    System.out.println("🛡️ " + messages.getString("goblinGeneral.duel.block"));
                    damageToPlayer = general.getBaseAttack() / 3;
                    if (random.nextInt(100) < 25) {
                        System.out.println("💥 " + messages.getString("goblinGeneral.duel.parry"));
                        damageToBoss = character.getAttack() / 2;
                    }
                }
                case "3" -> {
                    if (random.nextInt(100) < 40) {
                        System.out.println("🔥 " + messages.getString("goblinGeneral.duel.counter.success"));
                        damageToBoss = character.getAttack();
                    } else {
                        System.out.println("❌ " + messages.getString("goblinGeneral.duel.counter.fail"));
                        damageToPlayer = general.getBaseAttack();
                    }
                }
                default -> {
                    System.out.println(messages.getString("goblinGeneral.duel.hesitate"));
                    damageToPlayer = general.getBaseAttack();
                }
            }

            if (damageToBoss > 0) {
                general.takeDamage(damageToBoss);
                System.out.println("💥 " + messages.getString("goblinGeneral.duel.deal.damage") + damageToBoss);
            }
            if (damageToPlayer > 0) {
                character.takeDamage(damageToPlayer);
                System.out.println("😖 " + messages.getString("goblinGeneral.duel.take.damage") + damageToPlayer);
            }

            double bossHpPercent = (double) general.getHealth() / CharacterConstants.GoblinGeneral_HEALTH;
            if (bossHpPercent <= 0.2 && random.nextInt(100) < 50) {
                SlowPrinter.slowPrint("\n⚡ " + messages.getString("goblinGeneral.duel.boss.enrage"));
                general.superMove();
                SlowPrinter.slowPrint("\n⚡ " + messages.getString("goblinGeneral.duel.boss.heal"));
            }

            if (character.isDead() || general.isDead()) break;
            pause();
        }
    }
}
