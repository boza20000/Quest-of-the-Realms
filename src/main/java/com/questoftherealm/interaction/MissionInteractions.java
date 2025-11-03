package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.BattleFactory;
import com.questoftherealm.enemyEntities.entities.Goblin;
import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.ItemDrop;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static com.questoftherealm.expeditions.missions.Escape_to_Safety.*;

public class MissionInteractions {

    private static Random random() {
        return ThreadLocalRandom.current();
    }
    public static void worldStart() {
        Player player = Game.getPlayer();
        if (player.getCurMission() instanceof Meet_the_Elder && !player.getCurMission().isCompleted()) {
            String start = """
                    You enter the kings castle to talk to the castle's Elder and he wants
                    to help you to be better prepared for the journey that awaits you.
                    """;
            SlowPrinter.slowPrint(start);
        } else {
            System.out.println("You find yourself in " + player.getCurrentZone() + ".");
        }
    }

    public static void elderDialogue(String name, ItemDrop weapon, ItemDrop helmet, ItemDrop chestplate, ItemDrop boots) {
        String dialogue = """
                The eldar %s is one of the council's wisest people.
                He is urging you to prepare well for the expedition.
                He will give you some items to help you on your journey.
                """.formatted(name);
        SlowPrinter.slowPrint(dialogue);
        SlowPrinter.slowPrint("""
                A %s to protect yourself from what you meet in the north and on the way there.
                This weapon should provide the needed extra protection to reach the objective.
                You have received: %s
                """.formatted(weapon.item().getEffect(), weapon.item().getName()));
        SlowPrinter.slowPrint("""
                The king also gathered his best blacksmiths to make strong armor for you.
                You have received: %s, %s, %s
                """.formatted(helmet.item().getName(), chestplate.item().getName(), boots.item().getName()));
        SlowPrinter.slowPrint("""
                You’ll need rations before your journey.
                Seek them out in the village and surrounding lands.
                """);
    }

    public static void villagerDialogue(Player player, int villageId) {
        String dialogue;
        if (villageId == 1) {
            dialogue = """
                    The villager grips your arm, trembling.
                    "You’re too late %s... the monsters came at night. Shadows with glowing eyes.
                     They burned our homes. We barely escaped with our lives..."
                    """.formatted(player.getName());
        } else {
            dialogue = """
                    A wounded survivor speaks weakly:
                    "They came from the woods... not bandits, not men.
                     Creatures I’ve never seen before. They took our kids and butchered the whole village...
                     Beware the forests traveler.This is where they came from..."
                    """;
        }
        SlowPrinter.slowPrint(dialogue);
    }

    public static void villageIntro_1() {
        String dialog = """
                From distance you see...the village on fire
                with most houses already burned.
                A few villager spot you and carefully approach ...
                You should talk to them
                to understand the gravity of the situation and report to the castle.
                """;
        SlowPrinter.slowPrint(dialog);
    }

    public static void villageIntro_2() {
        String dialog = """
                You spot some smoke in the sky
                you start worrying..
                Was this village attacked as well?
                As you get closer you go pale on the spot
                You see a lone villager on the ground
                in front of a house and approach ...
                """;
        SlowPrinter.slowPrint(dialog);
    }

    public static void goblinCampSpotted() {
        String dialog = """
                You spot in the distance shadows moving, gathering near a camp in the forest...
                You found it?...
                The creatures that caused so much destruction are green and small
                with some bigger but don't seem that dangerous to burn two villages...
                How could they do so much harm?
                You go and investigate closer...
                """;
        SlowPrinter.slowPrint(dialog);
    }

    public static void goblinsTalkingOverheard() {
        String dialog = """
                You crouch behind a thicket, your breath shallow, eyes fixed on the flickering light of the goblin campfire.
                The guttural sounds of their speech cut through the night air...
                
                "South... we strike south next," one goblin snarls, slamming its fist into the dirt.
                "Big walls. Gold banners. The humans won't see it coming!"
                
                Another goblin chuckles, its teeth glinting in the firelight.
                "The King will be pleased. His reach grows every night..."
                
                A taller goblin in dark armor growls, silencing the others.
                "Do not speak his name so freely. The King has returned, but his eyes are everywhere now."
                
                The air feels heavier... unnatural. A strange hum seems to ripple through the ground beneath you.
                You lean closer, trying to hear more—
                
                *Snap!*
                
                A twig breaks under your boot.
                Several heads turn sharply toward your hiding spot.
                
                "What was that?" one hisses.
                "Human! By the trees!" another screeches.
                
                Torches flare to life.
                The goblins snarl and grab their weapons, rushing toward your position.
                
                You’ve been spotted.
                """;
        SlowPrinter.slowPrint(dialog);
    }

    public static void makeDecision(Player player) {
        System.out.println("\nThe goblins close in, snarling and waving their crude blades.");
        System.out.println("You have only moments to decide what to do...");
        System.out.println("""
                1️⃣  Try to escape into the woods.
                2️⃣  Draw your weapon and fight your way out.
                3️⃣  Use an item (potion, smoke bomb, etc.).
                4️⃣  Try to talk your way out.
                """);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> resolveEscape(player);
            case 2 -> resolveFight(player);
            case 3 -> resolveTalk(player);
            default -> {
                System.out.println("You freeze in fear... the goblins rush you!");
                resolveFight(player);
            }
        }
    }

    private static void resolveEscape(Player player) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println("""
                        You dash into the forest, branches slapping at your face.
                        Behind you, the goblins’ shouts fade into the distance — you made it!
                        """);
                playerEscapedAmbush = true;
            }
            case 1 -> {
                System.out.println("""
                        You sprint into the woods, but one goblin’s arrow grazes your arm!
                        You barely escape, wounded but alive...
                        """);
                player.getPlayerCharacter().takeDamage(10);
                playerEscapedAmbush = true;
            }
            case 2 -> {
                System.out.println("""
                        You run — but too late. A goblin leaps from the shadows and cuts you off!
                        You’re cornered. There’s no way out now. Fight him...
                        """);
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    private static void resolveFight(Player player) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println("You charge forward! The goblins scatter, terrified by your ferocity.");
                playerEscapedAmbush = true;
            }

            case 1 -> {
                System.out.println("You fight fiercely, cutting down two goblins as you escaped — but take a deep wound in your side.");
                player.getPlayerCharacter().takeDamage(15);
                playerEscapedAmbush = true;
            }
            case 2 -> {
                System.out.println("You swing your weapon wildly, but they overwhelm you!");
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    private static void resolveTalk(Player player) {
        int outcome = random().nextInt(3);
        switch (outcome) {
            case 0 -> {
                System.out.println("You bluff about serving their King. Confused, they let you go — for now.");
                playerEscapedAmbush = true;
            }

            case 1 -> {
                System.out.println("They pause, unsure, but still follow you at a distance — you slip away cautiously.");
                playerEscapedAmbush = true;
            }
            case 2 -> {
                System.out.println("They burst into laughter — then charge you!");
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    public static void reportToKing() {
        SlowPrinter.slowPrint("""
                🏰 You arrive at the Castle.
                The guards at the gates barely recognise you — dirt-streaked, armor dented, eyes weary from the northern wilds.
                They let you in.
                👑 Without hesitation, you rush through the great halls toward the King's chamber...
                
                The air inside is tense; whispers of war already hang like a storm about to break.
                """);

        SlowPrinter.slowPrint("""
                🧝‍♂️ You kneel, still covered in dust and scars from the journey.
                "My King... the rumors were true.
                 The villages... they weren’t raided by men or beasts — but by *goblins*."
                """);

        SlowPrinter.slowPrint("""
                👑 King: "Goblins? Impossible. They were scattered long ago during the First Wars."
                """);

        SlowPrinter.slowPrint("""
                🧝‍♂️ "I saw them with my own eyes, Your Majesty.
                 Small and green — some larger and armored. But they are many. Too many.
                 They have built a camp deep in the northern forests... and they are marching south."
                """);

        SlowPrinter.slowPrint("""
                👑 King (leaning forward): "Marching south? Toward *us*?"
                """);

        SlowPrinter.slowPrint("""
                🧝‍♂️ "Yes, my King. Their numbers are vast — an army beyond anything we’ve faced in generations.
                 Two villages have already been reduced to ashes. If we do not act now...
                 the realm of men will fall before the first snow."
                """);

        SlowPrinter.slowPrint("""
                👑 King (grimly): "Then war it shall be."
                He rises slowly, voice echoing through the hall.
                "Sound the horns. Summon the generals.
                 The armies of Eldoria will march north by dawn.
                 We will not wait for the shadows to reach our gates!"
                """);

        SlowPrinter.slowPrint("""
                The chamber erupts in shouts and movement — messengers rush off, the war banners are unfurled.
                You stand before your King, weary but resolute.
                
                👑 King: "You have done well, brave one. Rest for now.
                 Soon, all of Eldoria will owe its survival to your courage."
                
                ⚔️ The drums of war begin to thunder across the realm...
                """);
        King.hasTalkedToTheKing = true;
    }
}





