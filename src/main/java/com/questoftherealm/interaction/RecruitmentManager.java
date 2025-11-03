package com.questoftherealm.interaction;

import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.game.Game;
import java.util.Scanner;

public class RecruitmentManager {
    //private final Player player;
    //private final Assemble_an_Army mission;
    private static final Scanner scanner = new Scanner(System.in);

    private static int roll() {
        return RandomManger.random().nextInt(100);
    }
//    public RecruitmentManager(Player player, Random random, Scanner scanner, Assemble_an_Army mission) {
//        this.player = player;
//        this.random = random;
//        this.mission = mission;
//    }


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
        System.out.println("You speak of the goblin threat and the need for unity...");
        if (roll() < 50) {
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
                    if (roll() < 40) {
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
        System.out.println("You step closer, voice hard. 'You swore an oath to the King. Will you break it?'");
        if (roll() < 45) {
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

                    if (roll() < 30) {
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

        if (roll() < 35) {
            System.out.println("Archer Captain: 'We’ve been waiting for this moment. The goblins won’t know what hit them!'");
            Assemble_an_Army.archersRecruited = true;
        } else if (roll() < 80) {
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
                if (roll() < 40) {
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
        if (roll() < 50 && (input.equals("1") || input.equals("2"))) {
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

        if (roll() < 30) {
            System.out.println("Archmage: 'We foresaw your arrival. The stars align — we shall aid you.'");
            Assemble_an_Army.magesRecruited = true;
        } else if (roll() < 75) {
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
                if (roll() < 50) {
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
        if (roll() < 40 && (input.equals("1") || input.equals("2"))) {
            System.out.println("Your conviction pierces their pride. 'Very well,' says the Archmage. 'We will stand with you.'");
            Assemble_an_Army.magesRecruited = true;
        } else {
            System.out.println("The mages vanish in a swirl of mist, leaving you alone.");
        }
    }
}
