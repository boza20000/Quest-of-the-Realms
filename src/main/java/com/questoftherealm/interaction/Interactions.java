package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.BattleFactory;
import com.questoftherealm.enemyEntities.Enemy;
import com.questoftherealm.enemyEntities.EnemyFactory;
import com.questoftherealm.enemyEntities.EnemyType;
import com.questoftherealm.enemyEntities.bosses.GoblinGeneral;
import com.questoftherealm.enemyEntities.bosses.GoblinKing;
import com.questoftherealm.enemyEntities.entities.Goblin;
import com.questoftherealm.expeditions.missions.*;
import com.questoftherealm.friendlyEntities.Entities.Elder;
import com.questoftherealm.friendlyEntities.Entities.King;
import com.questoftherealm.game.Game;
import com.questoftherealm.game.GameConstants;
import com.questoftherealm.items.Chest;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemDrop;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.map.Event;
import com.questoftherealm.map.Locations;
import com.questoftherealm.map.TileTypes;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static com.questoftherealm.expeditions.missions.Escape_to_Safety.*;

public class Interactions {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

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

    private static final List<String> MOVE_CONNECTORS = List.of(
            "➡️ You leave the %s behind and make your way %s, the road stretching before you.",
            "🚶 From the %s, your journey takes you %s, with the wind brushing past as you walk.",
            "🌍 Departing the %s, you follow the worn path leading %s into the unknown.",
            "🧭 The %s fades behind you as you continue %s, footsteps steady and determined.",
            "🏞️ Moving away from the %s, the world ahead opens up as you travel %s.",
            "➡️ Quietly leaving the %s, you tread %s, your thoughts wandering with each step.",
            "🌅 Turning your back on the %s, you set off %s beneath the changing sky.",
            "🌾 The %s grows distant as you venture %s, each step taking you further from what you know."
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
        if (start == end) {
            return switch (end) {
                case MOUNTAIN ->
                        "⛰️ You continue winding along the rocky mountain paths, surrounded by towering cliffs and echoing winds.";
                case FOREST ->
                        "🌲 You press deeper into the forest, where the canopy thickens and shadows play across the mossy ground.";
                case SWAMP ->
                        "💧 The swamp stretches endlessly, each step sending ripples through murky water and whispering reeds.";
                case GRASS -> "🌾 The grasslands roll endlessly ahead, waves of green swaying under the open sky.";
                case VILLAGE ->
                        "🏘️ You wander through the village, passing familiar faces and hearing the soft hum of daily life.";
                case CASTLE ->
                        "🏰 You roam within the castle’s domain, its walls echoing the weight of stories and power.";
                case WATER ->
                        "💦 You remain near the water’s edge, where waves lap gently and the air smells of salt and cool mist.";
            };
        }
        return switch (end) {
            case MOUNTAIN ->
                    "⛰️ The air grows thinner and colder. The path ahead winds upward, becoming steep and treacherous.";
            case FOREST ->
                    "🌲 The trees close in around you. The canopy above blocks out most of the light, and the forest grows quiet.";
            case SWAMP ->
                    "💧 The ground grows damp and soft. The air becomes heavy with mist, and the smell of stagnant water fills your nose.";
            case GRASS ->
                    "🌾 The land opens into wide grasslands. The breeze carries the scent of wildflowers and sun-warmed earth.";
            case VILLAGE ->
                    "🏘️ You arrive at a village — laughter and chatter fill the air, and warm light spills from nearby windows.";
            case CASTLE ->
                    "🏰 The walls shrink behind you as you walk further. The world ahead feels less guarded, more uncertain.";
            case WATER ->
                    "💦 You hear the sound of waves and trickling streams. The ground softens, and the air carries a cool, refreshing scent.";
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
                BattleFactory.createBattle(player, new Goblin()).simulate();
            }
        }
    }

    private static void resolveFight(Player player) {
        int outcome = random.nextInt(3);
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

    // === KNIGHTS ===
    public static void talkToTheKnights() {
        Assemble_an_Army.knightsTriedToRecruit = true;
        if (Assemble_an_Army.knightsRecruited) {
            System.out.println("⚔️ The knights are already marching under your banner.");
            return;
        }

        System.out.println("\n🏰 You enter the Southern Garrison. The Knight Commander greets you coldly.");
        System.out.println("Commander: 'State your business, traveler.'");

        while (true) {
            System.out.println("\n1️⃣ 'The realm needs you, Commander.'");
            System.out.println("2️⃣ 'You swore an oath to the King — fulfill it!'");
            System.out.println("3️⃣ 'I won’t force you to fight. Farewell.'");
            System.out.print("> ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> persuasionBranch();
                case "2" -> intimidationBranch();
                case "3" -> {
                    System.out.println("You turn away. The Commander watches you go in silence.");
                    return;
                }
                default -> System.out.println("The Commander raises an eyebrow. 'Speak clearly, traveler.'");
            }


            if (Assemble_an_Army.knightsRecruited) return;
        }
    }

    private static void persuasionBranch() {
        int roll = random.nextInt(100);
        System.out.println("You speak of the goblin threat and the need for unity...");
        if (roll < 50) {
            System.out.println("Commander: 'Your words move me, hero. The knights will stand with you!'");
            Assemble_an_Army.knightsRecruited = true;
        } else {
            System.out.println("Commander: 'You speak well, but our men are weary and our coffers dry.'");
            System.out.println("1️⃣ Offer 50 gold for supplies\n2️⃣ Try to reason again\n3️⃣ Refuse");
            System.out.print("> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> payForKnights();
                case "2" -> {
                    int persuasion2 = random.nextInt(100);
                    if (persuasion2 < 40) {
                        System.out.println("Commander: 'Very well... You’ve convinced me. We’ll ride at dawn.'");
                        Assemble_an_Army.knightsRecruited = true;
                    } else {
                        System.out.println("Commander: 'Enough talk. Without resources, we can’t fight.'");
                    }
                }
                case "3" -> System.out.println("You leave empty-handed. The knights remain in their barracks.");
            }
        }
    }

    private static void intimidationBranch() {
        int roll = random.nextInt(100);
        System.out.println("You step closer, voice hard. 'You swore an oath to the King. Will you break it?'");
        if (roll < 45) {
            System.out.println("Commander: '...You’re right. Our duty is to the realm. Knights — to arms!'");
            Assemble_an_Army.knightsRecruited = true;
        } else {
            System.out.println("Commander: 'Watch your tone, outsider. I answer to no threats.'");
            System.out.println("1️⃣ Apologize and change approach\n2️⃣ Double down on intimidation\n3️⃣ Leave");
            System.out.print("> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> persuasionBranch();
                case "2" -> {
                    int roll2 = random.nextInt(100);
                    if (roll2 < 30) {
                        System.out.println("Your defiance surprises him. 'Hah! You’ve guts, at least. Fine — we’ll fight!'");
                        Assemble_an_Army.knightsRecruited = true;
                    } else {
                        System.out.println("The Commander orders you out. The guards glare as you leave.");
                    }
                }
                case "3" -> System.out.println("You walk away, tension thick in the air.");
            }
        }
    }

    private static void payForKnights() {
        if (Game.getPlayer().payMoney(50)) {
            System.out.println("💰 You hand over the gold. The Commander nods solemnly.");
            System.out.println("'Supplies will arrive by dusk. You have our swords, hero.'");
            Assemble_an_Army.knightsRecruited = true;
        } else {
            System.out.println("You don't have enough gold. The Commander sighs: 'Then there’s nothing I can do.'");
        }
    }


    // === ARCHERS ===
    public static void talkToTheArchers() {
        Assemble_an_Army.archersTriedToRecruit = true;
        if (Assemble_an_Army.archersRecruited) {
            System.out.println("🏹 The archers are already prepared for battle.");
            return;
        }

        System.out.println("\n🌲 You find the archers training in the forest clearing...");
        int roll = random.nextInt(100);

        if (roll < 35) {
            System.out.println("Archer Captain: 'We’ve been waiting for this moment. The goblins won’t know what hit them!'");
            Assemble_an_Army.archersRecruited = true;
        } else if (roll < 80) {
            System.out.println("Archer Captain: 'We could use better arrows — bring us goblin fletching, and we’ll join.'");
            System.out.println("1️⃣ Promise to get the fletching\n2️⃣ Offer 30 gold instead\n3️⃣ Threaten them to obey");
            handleArcherChoice();
        } else {
            System.out.println("Archer Captain: 'We’ve lost too many scouts. We’ll not fight again.'");
            System.out.println("1️⃣ Show your courage\n2️⃣ Offer protection\n3️⃣ Leave");
            handleArcherRefusalChoice();
        }
    }

    private static void handleArcherChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> System.out.println("You promise to return with goblin fletching. The archers nod cautiously.");
            case "2" -> {
                if (Game.getPlayer().payMoney(30)) {
                    System.out.println("💰 The gold convinces them. They agree to join the fight!");
                    Assemble_an_Army.archersRecruited = true;
                } else {
                    System.out.println("Not enough gold. The archers laugh and turn away.");
                }
            }
            case "3" -> {
                int roll = random.nextInt(100);
                if (roll < 40) {
                    System.out.println("They draw their bows... but your boldness earns their respect.");
                    Assemble_an_Army.archersRecruited = true;
                } else {
                    System.out.println("They refuse your threats. You barely leave unharmed.");
                }
            }
            default -> System.out.println("The archers ignore you.");
        }
    }

    private static void handleArcherRefusalChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        int roll = random.nextInt(100);
        if (roll < 50 && (input.equals("1") || input.equals("2"))) {
            System.out.println("Your passion stirs their spirits. The archers agree to join you!");
            Assemble_an_Army.archersRecruited = true;
        } else {
            System.out.println("They remain hidden in the forest, unwilling to fight.");
        }
    }

    // === MAGES ===
    public static void talkToTheMages() {
        Assemble_an_Army.magesTriedToRecruit = true;
        if (Assemble_an_Army.magesRecruited) {
            System.out.println("🪄 The mages are already preparing their spells for war.");
            return;
        }

        System.out.println("\n🔮 You enter the ancient outpost, where the Mages’ Council debates your request...");
        int roll = random.nextInt(100);

        if (roll < 30) {
            System.out.println("Archmage: 'We foresaw your arrival. The stars align — we shall aid you.'");
            Assemble_an_Army.magesRecruited = true;
        } else if (roll < 75) {
            System.out.println("Archmage: 'Magic is not cheap. Bring us 2 mana crystals, and we’ll fight beside you.'");
            System.out.println("1️⃣ Offer gold instead (70g)\n2️⃣ Try to reason with them\n3️⃣ Promise to return with crystals");
            handleMageChoice();
        } else {
            System.out.println("Archmage: 'The risk is too great. Our order must remain hidden.'");
            System.out.println("1️⃣ Appeal to their duty\n2️⃣ Challenge their cowardice\n3️⃣ Leave silently");
            handleMageRefusalChoice();
        }
    }

    private static void handleMageChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                if (Game.getPlayer().payMoney(70)) {
                    System.out.println("💰 The mages accept the gold and begin their preparations.");
                    Assemble_an_Army.magesRecruited = true;
                } else {
                    System.out.println("Not enough gold. The mages turn away silently.");
                }
            }
            case "2" -> {
                int persuasion = random.nextInt(100);
                if (persuasion < 50) {
                    System.out.println("Your reasoning convinces them that the realm’s survival is at stake.");
                    Assemble_an_Army.magesRecruited = true;
                } else {
                    System.out.println("They dismiss you with cold stares.");
                }
            }
            default -> System.out.println("You leave, vowing to return with the crystals.");
        }
    }

    private static void handleMageRefusalChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        int roll = random.nextInt(100);
        if (roll < 40 && (input.equals("1") || input.equals("2"))) {
            System.out.println("Your conviction pierces their pride. 'Very well,' says the Archmage. 'We will stand with you.'");
            Assemble_an_Army.magesRecruited = true;
        } else {
            System.out.println("The mages vanish in a swirl of mist, leaving you alone.");
        }
    }

    /// //////////////////////////////////////////////////////////////////////////////
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.out.println("Knights charge into goblin lines...");
        }
        if (Assemble_an_Army.magesRecruited) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.out.println("Mages unleash firestorms...");
        }
        if (Assemble_an_Army.archersRecruited) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.out.println("Archers rain death from above...");
        }

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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

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
                try {
                    Thread.sleep(800);
                } catch (InterruptedException ignored) {
                }
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

    public void exploreStructure(Locations structure, Player player) {
        SlowPrinter.slowPrint("\n🏰 You arrive at the " + structure.getName() + ".");
        SlowPrinter.slowPrint(structure.getDescription());
        SlowPrinter.slowPrint("What would you like to do?");
        SlowPrinter.slowPrint("1. Enter carefully");
        SlowPrinter.slowPrint("2. Observe from outside");
        SlowPrinter.slowPrint("3. Leave it alone");
        int choice = getChoice(3);
        switch (choice) {
            case 1 -> enterStructure(player, structure);
            case 2 -> observeStructure(player, structure);
            default -> SlowPrinter.slowPrint("You decide it's best not to meddle here and move on...");
        }
    }


    private static void enterStructure(Player player, Locations structure) {
        SlowPrinter.slowPrint("You take a deep breath and step inside...");
        pause();

        int outcome = random.nextInt(100);

        switch (structure) {
            case ABANDONED_TOWER, MAGES_TOWER -> exploreTower(player, outcome);
            case SHADOW_CAVERN, IRON_MINE -> exploreCave(player, outcome);
            case BANDIT_CAMP, FORGOTTEN_RUINS -> exploreRuins(player, outcome);
            case SACRED_GROVE, ANCIENT_ALTAR -> exploreSacredPlace(player, outcome);
            default -> exploreGeneric(player, outcome);
        }
    }

    private static void exploreTower(Player player, int outcome) {
        if (outcome < 30) {
            SlowPrinter.slowPrint("As you climb the stairs, a ghostly spirit materializes!");
            Enemy spirit = EnemyFactory.createEnemy(EnemyType.LOST_SPIRIT);
            spirit.interact(player);
        } else if (outcome < 60) {
            SlowPrinter.slowPrint("You find an old chest hidden under the stairs...");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("The tower is eerily silent. You find nothing but dust and echoes.");
        }
    }

    private static void exploreCave(Player player, int outcome) {
        if (outcome < 40) {
            SlowPrinter.slowPrint("You descend into darkness... glowing eyes appear in the shadows!");
            Enemy beast = EnemyFactory.createEnemy(EnemyType.WOLF);
            beast.interact(player);
        } else if (outcome < 70) {
            SlowPrinter.slowPrint("You find a strange crystal embedded in the rock. It hums with energy.");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("You wander the tunnels, but only find damp stone and silence.");
        }
    }

    private static void exploreRuins(Player player, int outcome) {
        if (outcome < 35) {
            SlowPrinter.slowPrint("You trigger an ancient trap! Arrows shoot from the walls!");
            player.getPlayerCharacter().takeDamage(10);
        } else if (outcome < 65) {
            SlowPrinter.slowPrint("Among the rubble, you discover a relic half-buried in moss...");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("The ruins whisper of old times... but offer no treasure today.");
        }
    }

    private static void exploreSacredPlace(Player player, int outcome) {
        Characters character = player.getPlayerCharacter();
        if (outcome < 25) {
            SlowPrinter.slowPrint("You anger the spirits of nature! Vines lash out at you!");
            character.takeDamage(8);
        } else if (outcome < 55) {
            SlowPrinter.slowPrint("A soothing aura fills the air. You feel healed.");
            character.setHealth(character.getHealth() + 10);
        } else {
            SlowPrinter.slowPrint("You find a glowing herb near the altar — it could be useful later.");
            findLoot(player);
        }
    }

    private static void exploreGeneric(Player player, int outcome) {
        if (outcome < 50) {
            SlowPrinter.slowPrint("You find something useful in the dust...");
            findLoot(player);
        } else {
            SlowPrinter.slowPrint("You explore for a while, but nothing stands out.");
        }
    }

    private static void observeStructure(Player player, Locations structure) {
        int roll = random.nextInt(100);
        if (roll < 30) {
            SlowPrinter.slowPrint("As you watch, something moves inside... you narrowly avoid being seen!");
        } else {
            SlowPrinter.slowPrint("You take some time to sketch and note your observations — perhaps it’ll help later.");
        }
    }

    private static void findLoot(Player player) {
        ItemDrop loot = Chest.generateRandomItem();
        player.getInventory().addItem(loot.item(), loot.quantity());
        SlowPrinter.slowPrint("🎁 You found a " + loot.item().getName() + "!");
    }

    private static int getChoice(int max) {
        System.out.print("> ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return Math.max(1, Math.min(choice, max));
        } catch (Exception e) {
            return 3; // default leave
        }
    }

    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}





