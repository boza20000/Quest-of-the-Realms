package com.questoftherealm.interaction;

import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinKing;
import com.questoftherealm.expeditions.missions.Breach_the_Stronghold;
import com.questoftherealm.expeditions.missions.Defeat_the_Goblin_King;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;

import java.util.Random;
import java.util.Scanner;

public class GoblinKingManager {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static void goblinKingdomFound() {
        SlowPrinter.slowPrint(
                """
                        🌄 You and your elite strike team reach the Far North Mountains...
                        The wind howls through jagged peaks as twilight fades into blackness.
                        Ahead, faint torches flicker — the entrance to the Goblin Kingdom.
                        
                        🕳️ You slip inside the cavern, the walls glowing faintly with strange fungus.
                        Voices echo deeper within — guttural and harsh. You creep closer...
                        
                        🔥 You peer around a bend and see a massive cavern filled with goblins.
                        Hundreds kneel before a towering goblin with a bone crown — the Goblin King.
                        He snarls in fury: 'Our armies… crushed by humans?! Useless worms!'
                        He grabs one of his generals by the throat and snaps his neck.
                        'Send word to the tribes in the Frozen Wastes! We will rebuild — BIGGER!'
                        
                        The goblins cheer, pounding their weapons on the ground in rage.
                        Your lieutenant whispers: 'If we act now, we can end this war before it begins.'
                        
                        You must decide the fate of this battle.
                        """
        );
        Breach_the_Stronghold.isBreached = true;

        System.out.println("\n1️⃣ Sneak through the stronghold and assassinate the King silently.");
        System.out.println("2️⃣ Lead an all-out assault and crush the goblin horde head-on!");
        int choice;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            choice = random.nextInt(1, 3);
        }
        if (choice == 1) {
            stealthInfiltration();
        } else {
            fullAssault();
        }
    }

    //aggressive infiltration
    private static void fullAssault() {
        SlowPrinter.slowPrint(
                """
                        ⚔️ You rally your team and raise your sword high!
                        'FOR THE REALM!' you cry — and charge into the heart of the stronghold.
                        
                        The goblins reel in shock as your warriors crash into their ranks.
                        Fire and steel fill the air — the tunnels burn red with war.
                        """
        );

        SlowPrinter.slowPrint(
                """
                        💥 The Goblin King roars in rage and flees deeper into the mountain.
                        You pursue — blades clash in the dark, until he turns to face you.
                        
                        The King strikes down one of your comrades with a crushing blow!
                        'You took my army,' he growls, 'but I’ll take your life!'
                        
                        The others fall back — this fight is yours alone.
                        """
        );

        System.out.println("🔥 Boss Fight Begins: The Goblin King!");
        startBossFight();
    }

    //silent approach
    private static void stealthInfiltration() {
        SlowPrinter.slowPrint(
                """
                        🕶️ You motion for silence and melt into the shadows.
                        Step by step, you and your team slip between patrols.
                        You dispatch guards with silent blades and muffled cries.
                        Each tunnel brings you closer to the throne chamber...
                        """
        );
        pause();

        SlowPrinter.slowPrint(
                """
                        💀 At last, you see him — the Goblin King upon his bone throne,
                        barking orders to his generals. He hasn’t noticed you.
                        
                        You count the guards — six of them. Your team moves in position.
                        
                        1️⃣ Take the shot — assassinate the guards and rush the King.
                        2️⃣ Wait for the perfect moment — strike when he’s alone.
                        """
        );

        int subChoice;
        try {
            subChoice = scanner.nextInt();
        } catch (Exception e) {
            subChoice = random.nextInt(1, 3);
        }
        if (subChoice == 1) {
            SlowPrinter.slowPrint(
                    """
                            ⚡ You give the signal — arrows and daggers fly!
                            The guards fall before they can raise the alarm.
                            The King roars in fury, summoning dark magic.
                            Together, you and your squad charge!
                            """
            );
            Defeat_the_Goblin_King.isDefeated = true;

            SlowPrinter.slowPrint("""
                    👑 The Goblin King falls beneath a storm of steel and fire.
                    🎉 The realm is saved — the goblin threat is ended at last!
                     You all go to the designated meeting spot with minimal casualties
                     and reunite with the rest of the group that intercepted the scouts.
                     Together you go out of the mountains and head to the castle...
                    """);

        } else {
            SlowPrinter.slowPrint(
                    """
                            ⏳ You wait... the King dismisses his generals.
                            The moment his guards leave, you strike like a shadow.
                            One clean thrust — and the war ends in silence.
                            """
            );
            Defeat_the_Goblin_King.isDefeated = true;
            SlowPrinter.slowPrint("""
                    ✨The Goblin King dies without a word. The mission is success...
                     You sneak back out of the cave..
                     After that you go to the designated meeting spot and reunite with the rest of the group.
                     Together you go out of the mountains and head to the castle...""");
        }
    }



    //1v1 with the king boss
    private static void startBossFight() {
        Characters character = Game.getPlayer().getPlayerCharacter();
        GoblinKing king = new GoblinKing();
        int round = 1;

        SlowPrinter.slowPrint(
                """
                        👑 The Goblin King towers before you, his armor glimmering red in the firelight.
                        He growls: "You think victory over my generals makes you a hero? Foolish mortal!"
                        Sparks fall from the cavern ceiling as the final battle begins...
                        """
        );

        while (!character.isDead() && !king.isDead()) {
            System.out.println("\n🔥 ROUND " + round++ + " 🔥");
            System.out.println("💚 Your HP: " + character.getHealth() + " | 🔵 Mana: " + character.getMana());
            System.out.println("❤️ Goblin King HP: " + king.getHealth());
            SlowPrinter.slowPrint("\n" + king.getName() + " raises his sword...");

            int bossMove = random.nextInt(5); // 0: heavy swing, 1: charge, 2: feint, 3: surprise attack, 4: super attack
            switch (bossMove) {
                case 0 -> System.out.println("⚔️ " + king.getName() + " prepares a crushing overhead swing!");
                case 1 -> System.out.println("🏃‍♂️ " + king.getName() + " charges forward with fury!");
                case 2 -> System.out.println("😈 " + king.getName() + " feints left, trying to bait your move!");
                case 3 ->
                        System.out.println("💨 " + king.getName() + " disappears into the shadows for a surprise strike!");
                case 4 ->
                        System.out.println("🌋 " + king.getName() + " channels dark fire — his ultimate attack is coming!");
            }

            System.out.println("\nYour action:");
            System.out.println("1️⃣ Dodge (uses Mana)");
            System.out.println("2️⃣ Block (reduces damage)");
            System.out.println("3️⃣ Counterattack (risky, high reward)");
            System.out.println("4️⃣ Hide behind a pillar (chance for surprise attack)");
            System.out.println("5️⃣ Use potion and attack (risky but powerful)");
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
                            System.out.println("💨 You roll aside — the King's blade slams into the stone floor!");
                        } else {
                            System.out.println("❌ Too slow! The edge grazes your arm!");
                            damageToPlayer = king.getBaseAttack() / 2;
                        }
                    } else {
                        System.out.println("⚠️ Not enough mana to dodge!");
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 2 -> { // Block
                    System.out.println("🛡️ You brace your weapon and prepare for the hit!");
                    damageToPlayer = king.getBaseAttack() / 3;
                    if (random.nextInt(100) < 30) {
                        System.out.println("💥 You parry his strike and counterattack!");
                        damageToBoss = character.getAttack() / 2;
                    }
                }
                case 3 -> { // Counterattack
                    System.out.println("⚔️ You watch his movements closely...");
                    if (random.nextInt(100) < 45) {
                        System.out.println("🔥 You strike just as he exposes his flank!");
                        damageToBoss = character.getAttack();
                    } else {
                        System.out.println("💀 You mistime your strike — the King smashes you aside!");
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 4 -> { // Hide behind a pillar
                    System.out.println("🏗️ You dive behind a pillar. The King roars in confusion...");
                    if (random.nextInt(100) < 60) {
                        System.out.println("🎯 You leap out and stab him in the back!");
                        damageToBoss = (int) (character.getAttack() * 1.3);
                    } else {
                        System.out.println("💀 The King smashes through the stone — debris slams into you!");
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                case 5 -> { // Potion + attack
                    try {
                        String item = scanner.nextLine();
                        Item i = ItemRegistry.getItem(item);
                        Game.getPlayer().useItem(i);
                    } catch (Exception e) {
                        System.out.println("⚠️ Item unavailable!");
                    }
                    System.out.println("💥 You charge in with reckless fury!");
                    if (random.nextInt(100) < 50) {
                        damageToBoss = (int) (character.getAttack() * 1.5);
                        System.out.println("🔥 Your blade cuts deep into the King's chest!");
                    } else {
                        System.out.println("😖 The King sidesteps and slashes your side!");
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                default -> {
                    System.out.println("😨 You hesitate — the Goblin King takes advantage!");
                    damageToPlayer = king.getBaseAttack();
                }
            }

            // Boss Super Attack (only if move 4)
            if (bossMove == 4 && random.nextInt(100) < 60) {
                System.out.println("🌪️ The King unleashes a wave of dark fire!");
                damageToPlayer += (int) (king.getBaseAttack() * 1.5);
            }

            // Enrage phase when HP < 20%
            if ((double) king.getHealth() / CharacterConstants.GoblinKing_HEALTH <= 0.2 && random.nextInt(100) < 40) {
                System.out.println("💢 The Goblin King bellows in fury — his strength surges!");
                king.setAttack((int) (king.getBaseAttack() * 1.3));
            }

            // Apply damage
            if (damageToBoss > 0) {
                king.takeDamage(damageToBoss);
                System.out.println("💥 You deal " + damageToBoss + " damage!");
            }
            if (damageToPlayer > 0) {
                character.takeDamage(damageToPlayer);
                System.out.println("😖 You take " + damageToPlayer + " damage!");
            }

            if (!king.isDead() && !character.isDead()) {
                pause();
            }
        }

        // === After the fight ===
        if (king.isDead()) {
            SlowPrinter.slowPrint(
                    """
                            ⚔️ With a final cry, you drive your weapon through the Goblin King's heart!
                            His crown shatters — his reign ends in silence.
                            👑 The Goblin Kingdom falls. The war is over.
                            """
            );
            Defeat_the_Goblin_King.isDefeated = true;
        } else {
            SlowPrinter.slowPrint(
                    """
                            💀 The Goblin King lets out a guttural laugh as his blade pierces your chest.
                            Darkness closes in... your vision fades.
                            The last thing you hear is the echo of his victory roar.
                            """
            );
            character.setHealth(0);
        }
    }

    private static void doge(){

    }

    private static void pause() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {
        }
    }
}
