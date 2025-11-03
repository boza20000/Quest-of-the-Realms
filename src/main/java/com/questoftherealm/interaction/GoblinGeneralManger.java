package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinGeneral;
import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_General;
import com.questoftherealm.game.Game;

import java.util.Random;
import java.util.Scanner;

public class GoblinGeneralManger {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static void startFinalBattle() {
        System.out.println("\n⚔️ The Battle for the Realm Begins!");
        System.out.println("Your army marches to meet the Goblin Horde under a blood-red sky...");

        int playerArmyPower = calculatePlayerArmyPower();
        int enemyArmyPower = 120 + random.nextInt(60);

        System.out.println("🏇 Your Army Power: " + playerArmyPower);
        System.out.println("👹 Goblin Army Power: " + enemyArmyPower);
        System.out.println("The armies clash in a storm of steel and fire...");
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

        System.out.println("\n💥 The battle rages on...");
        System.out.println("Drums thunder, arrows fly, and spells light the sky!");

        if (Assemble_an_Army.knightsRecruited) {
            pause();
            System.out.println("Knights charge into goblin lines...");
        }
        if (Assemble_an_Army.magesRecruited) {
            pause();
            System.out.println("Mages unleash firestorms...");
        }
        if (Assemble_an_Army.archersRecruited) {
            pause();
            System.out.println("Archers rain death from above...");
        }
        pause();
        if (roll < (winChance * 100)) {
            System.out.println("\n🏆 Your army has triumphed! The goblin horde breaks and flees!");
            System.out.println("But in the chaos, the Goblin General himself appears...");
            duelGoblinGeneral();
        } else {
            System.out.println("\n💀 Your army is overwhelmed... You fall in battle.");
            Game.getPlayer().getPlayerCharacter().setHealth(0);
            System.out.println("You were slain defending the realm.");
        }
    }

    private static void pause(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            System.out.println("sleep method error");
        }
    }

    private static void duelGoblinGeneral() {
        Player player = Game.getPlayer();
        goblinGeneralFight(player);
        if (!player.getPlayerCharacter().isDead()) {
            System.out.println("\n🔥 With one final blow, you slay the Goblin General!");
            Defeat_the_Goblin_General.isDefeated = true;
            System.out.println("🎉 The realm is safe... for now.");
        } else {
            System.out.println("\n💀 The Goblin General strikes you down. Darkness takes you...");
            Game.getPlayer().getPlayerCharacter().setHealth(0);
            System.out.println("Defeated by the Goblin General.");
        }
    }

    private static void goblinGeneralFight(Player player) {
        GoblinGeneral general = new GoblinGeneral();
        Characters character = player.getPlayerCharacter();
        System.out.println("\n👹 The Goblin General roars: 'You will fall like your king, human!'");
        int round = 1;

        while (!character.isDead() && !general.isDead()) {
            System.out.println("\n⚔️ Round " + round++);
            System.out.println("💚 Your HP: " + character.getHealth() + " | 🔵 Mana: " + character.getMana());
            System.out.println("❤️ Goblin General HP: " + general.getHealth());
            SlowPrinter.slowPrint("\n" + general.getName() + " raises his weapon...");

            int bossMove = random.nextInt(3); // 0: heavy swing, 1: charge, 2: feint
            switch (bossMove) {
                case 0 -> System.out.println("⚔️ Azok prepares a heavy overhead swing!");
                case 1 -> System.out.println("🏃‍♂️ Azok charges forward!");
                case 2 -> System.out.println("😈 Azok feints to your left!");
            }

            System.out.println("\nYour action:");
            System.out.println("1️⃣ Dodge (uses  Mana)");
            System.out.println("2️⃣ Block (reduces damage)");
            System.out.println("3️⃣ Counterattack (risky, high reward)");
            System.out.print("Choose: ");
            String input = scanner.nextLine();

            int damageToBoss = 0;
            int damageToPlayer = 0;

            // === PLAYER ACTIONS ===
            switch (input) {
                case "1" -> { // Dodge
                    if (character.getMana() >= 5) {
                        character.setMana(character.getMana() - 5);
                        if (random.nextInt(100) < 60) {
                            System.out.println("💨 You roll away just in time — no damage!");
                        } else {
                            System.out.println("❌ Too slow! You get clipped by his attack!");
                            damageToPlayer = general.getBaseAttack() / 2;
                        }
                    } else {
                        System.out.println("⚠️ Not enough mana! You fail to dodge!");
                        damageToPlayer = general.getBaseAttack();
                    }
                }
                case "2" -> { // Block
                    System.out.println("🛡️ You brace for impact!");
                    damageToPlayer = general.getBaseAttack() / 3;
                    if (random.nextInt(100) < 25) {
                        System.out.println("💥 You parry and counter!");
                        damageToBoss = character.getAttack() / 2;
                    }
                }
                case "3" -> { // Counterattack
                    if (random.nextInt(100) < 40) {
                        System.out.println("🔥 You strike during his swing — a solid hit!");
                        damageToBoss = character.getAttack();
                    } else {
                        System.out.println("❌ Your counter fails! You’re open to attack!");
                        damageToPlayer = general.getBaseAttack();
                    }
                }
                default -> {
                    System.out.println("You hesitate, and Azok attacks!");
                    damageToPlayer = general.getBaseAttack();
                }
            }

            // === APPLY DAMAGE ===
            if (damageToBoss > 0) {
                general.takeDamage(damageToBoss);
                System.out.println("💥 You deal " + damageToBoss + " damage!");
            }
            if (damageToPlayer > 0) {
                character.takeDamage(damageToPlayer);
                System.out.println("😖 You take " + damageToPlayer + " damage!");
            }

            // === SPECIAL MOVE ===
            double bossHpPercent = (double) general.getHealth() / CharacterConstants.GoblinGeneral_HEALTH;
            if (bossHpPercent <= 0.2 && random.nextInt(100) < 50) {
                SlowPrinter.slowPrint("\n⚡ Azok lets out a monstrous roar — he’s enraged!");
                general.superMove();
                SlowPrinter.slowPrint("\n⚡ Azok drinks some potion... He regains some health!");
            }

            if (character.isDead() || general.isDead()) break;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

        }
    }
}
