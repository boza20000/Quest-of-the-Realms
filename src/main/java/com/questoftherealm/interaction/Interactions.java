package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.enemyEntities.Battle;
import com.questoftherealm.enemyEntities.BattleFactory;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyConstants;
import com.questoftherealm.enemyEntities.entities.Goblin;
import com.questoftherealm.expeditions.missions.Ambushed;
import com.questoftherealm.game.Game;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.map.Event;
import com.questoftherealm.map.Tile;
import com.questoftherealm.map.TileTypes;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static com.questoftherealm.expeditions.missions.Ambushed.playerEscapedAmbush;

public class Interactions {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static void worldStart() {
        String start = """
                You enter the kings castle to talk to the castle's Elder and he wants
                to help you to be better prepared for the journey that awaits you.
                """;
        SlowPrinter.slowPrint(start);
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

    public static void traderDialogue() {

    }

    private static final List<String> MOVE_CONNECTORS = List.of(
            "➡️ You depart from the %s and begin walking %s.",
            "🚶 Leaving the %s behind, you head %s.",
            "🌍 From the %s, your journey continues %s.",
            "🧭 You leave the %s, setting off %s.",
            "🏞️ With the %s behind you, the road ahead leads %s.",
            "🪶 Slowly leaving the %s, you turn %s."
    );

    private static void randomTravelText(String direction, TileTypes start) {
        String line = MOVE_CONNECTORS.get(random.nextInt(MOVE_CONNECTORS.size()));
        SlowPrinter.slowPrint(line.formatted(start, direction));
    }

    private static void randomEvent(TileTypes type) {
        Event event = Event.generateEvent(type);
        SlowPrinter.slowPrint("⚠️ While traveling, you stumble upon: " + event.getName());
        SlowPrinter.slowPrint(event.getDescription());
        boolean investigate = promptYesNo("Do you want to investigate?");
        if (investigate) {
            SlowPrinter.slowPrint("👉 You decide to face it head-on!");
            event.getNpc().interact(Game.getPlayer());
            // TODO: Trigger combat, trade, or special logic based on event
        } else {
            SlowPrinter.slowPrint("➡️ You ignore it and continue your journey...");
        }
    }

    public static boolean promptYesNo(String question) {
        SlowPrinter.slowPrint(question + " (yes/no)");
        while (true) {
            System.out.print(">");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes") || input.equals("y")) return true;
            if (input.equals("no") || input.equals("n")) return false;
            System.out.println("Please type yes or no.");
        }
    }

    public static void pathInteraction(TileTypes start, String direction) {
        int roll = random.nextInt(100);
        if (roll < 30) { // 30% chance
            randomEvent(start);
        } else {
            randomTravelText(direction, start);
        }
    }

    public static String getTransition(TileTypes start, TileTypes end) {
        String from = start.toString().toLowerCase();
        return switch (end) {
            case MOUNTAIN ->
                    "⛰️ Leaving the %s behind, the air grows thinner and colder. The path ahead winds upward, becoming steep and treacherous."
                            .formatted(from);
            case FOREST ->
                    "🌲 Moving away from the %s, the trees close in around you. The canopy above blocks out most of the light, and the forest grows quiet."
                            .formatted(from);
            case SWAMP ->
                    "💧 As you depart the %s, the ground grows damp and soft. The air becomes heavy with mist, and the smell of stagnant water fills your nose."
                            .formatted(from);
            case GRASS ->
                    "🌾 Leaving the %s, the land opens into wide grasslands. The breeze carries the scent of wildflowers and sun-warmed earth."
                            .formatted(from);
            case VILLAGE ->
                    "🏘️ Stepping away from %s, the chatter of villagers fades. You find yourself on quieter paths leading to the unknown."
                            .formatted(from);
            case CASTLE ->
                    "🏰 The walls of the %s shrink behind you as you walk further. The world ahead feels less guarded, more uncertain."
                            .formatted(from);
            case WATER ->
                    "💦 After leaving the %s, you hear the sound of waves and trickling streams. The ground softens, and the air carries a cool, refreshing scent."
                            .formatted(from);
        };
    }

    private static final List<String> SPOTTING_CONNECTORS = List.of(
            "👀 In the distance, you spot %s.",
            "🏕️ As you travel, you come across %s.",
            "🌄 On the horizon, you see %s.",
            "🔎 Your eyes catch sight of %s nearby.",
            "🌲 Between the trees, %s comes into view.",
            "⚔️ You notice %s standing ahead of you.",
            "✨ Unexpectedly, you stumble upon %s.",
            "🌍 As the path bends, you discover %s.",
            "🔥 Smoke or movement draws your attention to %s.",
            "🏚️ Hidden among the landscape, you find %s."
    );

    public static String getRandomSpotting(String locationName) {
        String template = SPOTTING_CONNECTORS.get(random.nextInt(SPOTTING_CONNECTORS.size()));
        return template.formatted(locationName);
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

    public static void villageIntro_1(){
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
        int outcome = random.nextInt(3);
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
                BattleFactory.createBattle(player,new Goblin()).simulate();

            }
        }
    }

    private static void resolveFight(Player player) {
        int outcome = random.nextInt(3);
        switch (outcome) {
            case 0 ->{
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
        int outcome = random.nextInt(3);
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





}

