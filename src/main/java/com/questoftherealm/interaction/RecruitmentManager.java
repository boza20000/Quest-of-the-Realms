package com.questoftherealm.interaction;

import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.game.Game;
import com.questoftherealm.localization.MessageBundle;

import java.util.Scanner;

public class RecruitmentManager {
    private static final Scanner scanner = new Scanner(System.in);

    private static int roll() {
        return RandomManger.random().nextInt(100);
    }

    // === KNIGHTS ===
    public static void talkToTheKnights() {
        Assemble_an_Army.knightsTriedToRecruit = true;
        if (Assemble_an_Army.knightsRecruited) {
            System.out.println("⚔️ " + MessageBundle.get("recruit.knights.already"));
            return;
        }

        System.out.println();
        System.out.println("🏰 " + MessageBundle.get("recruit.knights.enter"));
        System.out.println(MessageBundle.get("recruit.knights.commander.greet"));

        while (true) {
            System.out.println();
            System.out.println("1️⃣ " + MessageBundle.get("recruit.knights.choice.1"));
            System.out.println("2️⃣ " + MessageBundle.get("recruit.knights.choice.2"));
            System.out.println("3️⃣ " + MessageBundle.get("recruit.knights.choice.3"));
            System.out.print("> ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> persuasionBranch();
                case "2" -> intimidationBranch();
                case "3" -> {
                    System.out.println(MessageBundle.get("recruit.knights.leave"));
                    return;
                }
                default -> System.out.println(MessageBundle.get("recruit.knights.invalid"));
            }

            if (Assemble_an_Army.knightsRecruited) return;
        }
    }

    private static void persuasionBranch() {
        System.out.println(MessageBundle.get("recruit.knights.persuade.start"));
        if (roll() < 50) {
            System.out.println(MessageBundle.get("recruit.knights.persuade.success"));
            Assemble_an_Army.knightsRecruited = true;
        } else {
            System.out.println(MessageBundle.get("recruit.knights.persuade.fail"));
            System.out.println(MessageBundle.get("recruit.knights.persuade.options"));
            System.out.print("> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> payForKnights();
                case "2" -> {
                    if (roll() < 40) {
                        System.out.println(MessageBundle.get("recruit.knights.persuade.reason.success"));
                        Assemble_an_Army.knightsRecruited = true;
                    } else {
                        System.out.println(MessageBundle.get("recruit.knights.persuade.reason.fail"));
                    }
                }
                case "3" -> System.out.println(MessageBundle.get("recruit.knights.persuade.refuse"));
            }
        }
    }

    private static void intimidationBranch() {
        System.out.println(MessageBundle.get("recruit.knights.intimidate.start"));
        if (roll() < 45) {
            System.out.println(MessageBundle.get("recruit.knights.intimidate.success"));
            Assemble_an_Army.knightsRecruited = true;
        } else {
            System.out.println(MessageBundle.get("recruit.knights.intimidate.fail"));
            System.out.println(MessageBundle.get("recruit.knights.intimidate.options"));
            System.out.print("> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> persuasionBranch();
                case "2" -> {
                    if (roll() < 30) {
                        System.out.println(MessageBundle.get("recruit.knights.intimidate.double.success"));
                        Assemble_an_Army.knightsRecruited = true;
                    } else {
                        System.out.println(MessageBundle.get("recruit.knights.intimidate.double.fail"));
                    }
                }
                case "3" -> System.out.println(MessageBundle.get("recruit.knights.intimidate.leave"));
            }
        }
    }

    private static void payForKnights() {
        if (Game.getPlayer().payMoney(50)) {
            System.out.println("💰 " + MessageBundle.get("recruit.knights.pay.success"));
            Assemble_an_Army.knightsRecruited = true;
        } else {
            System.out.println(MessageBundle.get("recruit.knights.pay.fail"));
        }
    }

    // === ARCHERS ===
    public static void talkToTheArchers() {
        Assemble_an_Army.archersTriedToRecruit = true;
        if (Assemble_an_Army.archersRecruited) {
            System.out.println("🏹 " + MessageBundle.get("recruit.archers.already"));
            return;
        }

        System.out.println();
        System.out.println("🌲 " + MessageBundle.get("recruit.archers.find"));

        int roll = roll();
        if (roll < 35) {
            System.out.println(MessageBundle.get("recruit.archers.eager"));
            Assemble_an_Army.archersRecruited = true;
        } else if (roll < 80) {
            System.out.println(MessageBundle.get("recruit.archers.resource"));
            System.out.println(MessageBundle.get("recruit.archers.resource.options"));
            handleArcherChoice();
        } else {
            System.out.println(MessageBundle.get("recruit.archers.refuse"));
            System.out.println(MessageBundle.get("recruit.archers.refuse.options"));
            handleArcherRefusalChoice();
        }
    }

    private static void handleArcherChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> System.out.println(MessageBundle.get("recruit.archers.promise"));
            case "2" -> {
                if (Game.getPlayer().payMoney(30)) {
                    System.out.println("💰 " + MessageBundle.get("recruit.archers.gold.success"));
                    Assemble_an_Army.archersRecruited = true;
                } else {
                    System.out.println(MessageBundle.get("recruit.archers.gold.fail"));
                }
            }
            case "3" -> {
                if (roll() < 40) {
                    System.out.println(MessageBundle.get("recruit.archers.threat.success"));
                    Assemble_an_Army.archersRecruited = true;
                } else {
                    System.out.println(MessageBundle.get("recruit.archers.threat.fail"));
                }
            }
            default -> System.out.println(MessageBundle.get("recruit.archers.ignore"));
        }
    }

    private static void handleArcherRefusalChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        if (roll() < 50 && (input.equals("1") || input.equals("2"))) {
            System.out.println(MessageBundle.get("recruit.archers.courage.success"));
            Assemble_an_Army.archersRecruited = true;
        } else {
            System.out.println(MessageBundle.get("recruit.archers.courage.fail"));
        }
    }

    // === MAGES ===
    public static void talkToTheMages() {
        Assemble_an_Army.magesTriedToRecruit = true;
        if (Assemble_an_Army.magesRecruited) {
            System.out.println("🪄 " + MessageBundle.get("recruit.mages.already"));
            return;
        }

        System.out.println();
        System.out.println("🔮 " + MessageBundle.get("recruit.mages.enter"));

        int roll = roll();
        if (roll < 30) {
            System.out.println(MessageBundle.get("recruit.mages.prophecy"));
            Assemble_an_Army.magesRecruited = true;
        } else if (roll < 75) {
            System.out.println(MessageBundle.get("recruit.mages.price"));
            System.out.println(MessageBundle.get("recruit.mages.price.options"));
            handleMageChoice();
        } else {
            System.out.println(MessageBundle.get("recruit.mages.refuse"));
            System.out.println(MessageBundle.get("recruit.mages.refuse.options"));
            handleMageRefusalChoice();
        }
    }

    private static void handleMageChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                if (Game.getPlayer().payMoney(70)) {
                    System.out.println("💰 " + MessageBundle.get("recruit.mages.gold.success"));
                    Assemble_an_Army.magesRecruited = true;
                } else {
                    System.out.println(MessageBundle.get("recruit.mages.gold.fail"));
                }
            }
            case "2" -> {
                if (roll() < 50) {
                    System.out.println(MessageBundle.get("recruit.mages.reason.success"));
                    Assemble_an_Army.magesRecruited = true;
                } else {
                    System.out.println(MessageBundle.get("recruit.mages.reason.fail"));
                }
            }
            default -> System.out.println(MessageBundle.get("recruit.mages.crystals"));
        }
    }

    private static void handleMageRefusalChoice() {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        if (roll() < 40 && (input.equals("1") || input.equals("2"))) {
            System.out.println(MessageBundle.get("recruit.mages.convince.success"));
            Assemble_an_Army.magesRecruited = true;
        } else {
            System.out.println(MessageBundle.get("recruit.mages.convince.fail"));
        }
    }
}
