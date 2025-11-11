package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Assemble_an_Army;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.localization.MessageBundle;

import java.util.Scanner;

public class RecruitmentManager {
    private static final Scanner scanner = new Scanner(System.in);

    private static int roll() {
        return RandomManger.random().nextInt(100);
    }

    // === KNIGHTS ===
    public void talkToTheKnights(Player player) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) {
            return;
        }
        q.setKnightsTriedToRecruit(true);
        if (q.isKnightsRecruited()) {
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
                case "1" -> persuasionBranch(player, q);
                case "2" -> intimidationBranch(player, q);
                case "3" -> {
                    System.out.println(MessageBundle.get("recruit.knights.leave"));
                    return;
                }
                default -> System.out.println(MessageBundle.get("recruit.knights.invalid"));
            }

            if (q.isKnightsRecruited()) return;
        }
    }

    private void persuasionBranch(Player player, RiseOfTheGoblinThreat q) {
        System.out.println(MessageBundle.get("recruit.knights.persuade.start"));
        if (roll() < 50) {
            System.out.println(MessageBundle.get("recruit.knights.persuade.success"));
            q.setKnightsRecruited(true);
        } else {
            System.out.println(MessageBundle.get("recruit.knights.persuade.fail"));
            System.out.println(MessageBundle.get("recruit.knights.persuade.options"));
            System.out.print("> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> payForKnights(player, q);
                case "2" -> {
                    if (roll() < 40) {
                        System.out.println(MessageBundle.get("recruit.knights.persuade.reason.success"));
                        q.setKnightsRecruited(true);
                    } else {
                        System.out.println(MessageBundle.get("recruit.knights.persuade.reason.fail"));
                    }
                }
                case "3" -> System.out.println(MessageBundle.get("recruit.knights.persuade.refuse"));
            }
        }
    }

    private void intimidationBranch(Player player, RiseOfTheGoblinThreat q) {
        System.out.println(MessageBundle.get("recruit.knights.intimidate.start"));
        if (roll() < 45) {
            System.out.println(MessageBundle.get("recruit.knights.intimidate.success"));
            q.setKnightsRecruited(true);
        } else {
            System.out.println(MessageBundle.get("recruit.knights.intimidate.fail"));
            System.out.println(MessageBundle.get("recruit.knights.intimidate.options"));
            System.out.print("> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> persuasionBranch(player, q);
                case "2" -> {
                    if (roll() < 30) {
                        System.out.println(MessageBundle.get("recruit.knights.intimidate.double.success"));
                        q.setKnightsRecruited(true);
                    } else {
                        System.out.println(MessageBundle.get("recruit.knights.intimidate.double.fail"));
                    }
                }
                case "3" -> System.out.println(MessageBundle.get("recruit.knights.intimidate.leave"));
            }
        }
    }

    private void payForKnights(Player player, RiseOfTheGoblinThreat q) {
        if (player.payMoney(50)) {
            System.out.println("💰 " + MessageBundle.get("recruit.knights.pay.success"));
            q.setKnightsRecruited(true);
        } else {
            System.out.println(MessageBundle.get("recruit.knights.pay.fail"));
        }
    }

    // === ARCHERS ===
    public void talkToTheArchers(Player player) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) {
            return;
        }
        q.setArchersTriedToRecruit(true);
        if (q.isArchersRecruited()) {
            System.out.println("🏹 " + MessageBundle.get("recruit.archers.already"));
            return;
        }

        System.out.println();
        System.out.println("🌲 " + MessageBundle.get("recruit.archers.find"));

        int roll = roll();
        if (roll < 35) {
            System.out.println(MessageBundle.get("recruit.archers.eager"));
            q.setArchersRecruited(true);
        } else if (roll < 80) {
            System.out.println(MessageBundle.get("recruit.archers.resource"));
            System.out.println(MessageBundle.get("recruit.archers.resource.options"));
            handleArcherChoice(player, q);
        } else {
            System.out.println(MessageBundle.get("recruit.archers.refuse"));
            System.out.println(MessageBundle.get("recruit.archers.refuse.options"));
            handleArcherRefusalChoice(player, q);
        }
    }

    private void handleArcherChoice(Player player, RiseOfTheGoblinThreat q) {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> System.out.println(MessageBundle.get("recruit.archers.promise"));
            case "2" -> {
                if (player.payMoney(30)) {
                    System.out.println("💰 " + MessageBundle.get("recruit.archers.gold.success"));
                    q.setArchersRecruited(true);
                } else {
                    System.out.println(MessageBundle.get("recruit.archers.gold.fail"));
                }
            }
            case "3" -> {
                if (roll() < 40) {
                    System.out.println(MessageBundle.get("recruit.archers.threat.success"));
                    q.setArchersRecruited(true);
                } else {
                    System.out.println(MessageBundle.get("recruit.archers.threat.fail"));
                }
            }
            default -> System.out.println(MessageBundle.get("recruit.archers.ignore"));
        }
    }

    private void handleArcherRefusalChoice(Player player, RiseOfTheGoblinThreat q) {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        if (roll() < 50 && (input.equals("1") || input.equals("2"))) {
            System.out.println(MessageBundle.get("recruit.archers.courage.success"));
            q.setArchersRecruited(true);
        } else {
            System.out.println(MessageBundle.get("recruit.archers.courage.fail"));
        }
    }

    // === MAGES ===
    public void talkToTheMages(Player player) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) {
            return;
        }
        q.setMagesTriedToRecruit(true);
        if (q.isMagesRecruited()) {
            System.out.println("🪄 " + MessageBundle.get("recruit.mages.already"));
            return;
        }

        System.out.println();
        System.out.println("🔮 " + MessageBundle.get("recruit.mages.enter"));

        int roll = roll();
        if (roll < 30) {
            System.out.println(MessageBundle.get("recruit.mages.prophecy"));
            q.setMagesRecruited(true);
        } else if (roll < 75) {
            System.out.println(MessageBundle.get("recruit.mages.price"));
            System.out.println(MessageBundle.get("recruit.mages.price.options"));
            handleMageChoice(player, q);
        } else {
            System.out.println(MessageBundle.get("recruit.mages.refuse"));
            System.out.println(MessageBundle.get("recruit.mages.refuse.options"));
            handleMageRefusalChoice(player, q);
        }
    }

    private void handleMageChoice(Player player, RiseOfTheGoblinThreat q) {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                if (player.payMoney(70)) {
                    System.out.println("💰 " + MessageBundle.get("recruit.mages.gold.success"));
                    q.setMagesRecruited(true);
                } else {
                    System.out.println(MessageBundle.get("recruit.mages.gold.fail"));
                }
            }
            case "2" -> {
                if (roll() < 50) {
                    System.out.println(MessageBundle.get("recruit.mages.reason.success"));
                    q.setMagesRecruited(true);
                } else {
                    System.out.println(MessageBundle.get("recruit.mages.reason.fail"));
                }
            }
            default -> System.out.println(MessageBundle.get("recruit.mages.crystals"));
        }
    }

    private void handleMageRefusalChoice(Player player, RiseOfTheGoblinThreat q) {
        System.out.print("Choose: ");
        String input = scanner.nextLine();
        if (roll() < 40 && (input.equals("1") || input.equals("2"))) {
            System.out.println(MessageBundle.get("recruit.mages.convince.success"));
            q.setMagesRecruited(true);
        } else {
            System.out.println(MessageBundle.get("recruit.mages.convince.fail"));
        }
    }

    private RiseOfTheGoblinThreat checkSafe(Player player) {
        if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && player.getCurMission() instanceof Assemble_an_Army) {
            return q;
        }
        return null;
    }
}
