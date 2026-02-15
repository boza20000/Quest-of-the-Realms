package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

public class RecruitmentManager {
    private Output output;

    public RecruitmentManager(GameState state) {
        this.output = state.getGameServices().getOutput();
    }

    private int roll(GameState state) {
        return state.getGameServices().getRandom().random().nextInt(100);
    }

    // === KNIGHTS ===
    public void talkToTheKnights(Player player, GameState state) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) {
            return;
        }
        q.setKnightsTriedToRecruit(true);
        if (q.isKnightsRecruited()) {
            output.println("⚔️ " + state.getMessages().getBundle().get("recruit.knights.already"));
            return;
        }

        output.println();
        output.println("🏰 " + state.getMessages().getBundle().get("recruit.knights.enter"));
        output.println(state.getMessages().getBundle().get("recruit.knights.commander.greet"));

        while (true) {
            output.println();
            output.println("1️⃣ " + state.getMessages().getBundle().get("recruit.knights.choice.1"));
            output.println("2️⃣ " + state.getMessages().getBundle().get("recruit.knights.choice.2"));
            output.println("3️⃣ " + state.getMessages().getBundle().get("recruit.knights.choice.3"));
            output.print("> ");
            String choice = state.getGameServices().getInput().nextLine();

            switch (choice) {
                case "1" -> persuasionBranch(player, q, state);
                case "2" -> intimidationBranch(player, q, state);
                case "3" -> {
                    output.println(state.getMessages().getBundle().get("recruit.knights.leave"));
                    return;
                }
                default -> output.println(state.getMessages().getBundle().get("recruit.knights.invalid"));
            }

            if (q.isKnightsRecruited()) return;
        }
    }

    private void persuasionBranch(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output.println(state.getMessages().getBundle().get("recruit.knights.persuade.start"));
        if (roll(state) < 50) {
            output.println(state.getMessages().getBundle().get("recruit.knights.persuade.success"));
            q.setKnightsRecruited(true);
        } else {
            output.println(state.getMessages().getBundle().get("recruit.knights.persuade.fail"));
            output.println(state.getMessages().getBundle().get("recruit.knights.persuade.options"));
            output.print("> ");
            String choice = state.getGameServices().getInput().nextLine();
            switch (choice) {
                case "1" -> payForKnights(player, q, state);
                case "2" -> {
                    if (roll(state) < 40) {
                        output.println(state.getMessages().getBundle().get("recruit.knights.persuade.reason.success"));
                        q.setKnightsRecruited(true);
                    } else {
                        output.println(state.getMessages().getBundle().get("recruit.knights.persuade.reason.fail"));
                    }
                }
                case "3" -> output.println(state.getMessages().getBundle().get("recruit.knights.persuade.refuse"));
            }
        }
    }

    private void intimidationBranch(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output.println(state.getMessages().getBundle().get("recruit.knights.intimidate.start"));
        if (roll(state) < 45) {
            output.println(state.getMessages().getBundle().get("recruit.knights.intimidate.success"));
            q.setKnightsRecruited(true);
        } else {
            output.println(state.getMessages().getBundle().get("recruit.knights.intimidate.fail"));
            output.println(state.getMessages().getBundle().get("recruit.knights.intimidate.options"));
            output.print("> ");
            String choice = state.getGameServices().getInput().nextLine();
            switch (choice) {
                case "1" -> persuasionBranch(player, q, state);
                case "2" -> {
                    if (roll(state) < 30) {
                        output.println(state.getMessages().getBundle().get("recruit.knights.intimidate.double.success"));
                        q.setKnightsRecruited(true);
                    } else {
                        output.println(state.getMessages().getBundle().get("recruit.knights.intimidate.double.fail"));
                    }
                }
                case "3" -> output.println(state.getMessages().getBundle().get("recruit.knights.intimidate.leave"));
            }
        }
    }

    private void payForKnights(Player player, RiseOfTheGoblinThreat q, GameState state) {
        if (player.payMoney(50, state)) {
            output.println("💰 " + state.getMessages().getBundle().get("recruit.knights.pay.success"));
            q.setKnightsRecruited(true);
        } else {
            output.println(state.getMessages().getBundle().get("recruit.knights.pay.fail"));
        }
    }

    // === ARCHERS ===
    public void talkToTheArchers(Player player, GameState state) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) {
            return;
        }
        q.setArchersTriedToRecruit(true);
        if (q.isArchersRecruited()) {
            output.println("🏹 " + state.getMessages().getBundle().get("recruit.archers.already"));
            return;
        }

        output.println();
        output.println("🌲 " + state.getMessages().getBundle().get("recruit.archers.find"));

        int roll = roll(state);
        if (roll < 35) {
            output.println(state.getMessages().getBundle().get("recruit.archers.eager"));
            q.setArchersRecruited(true);
        } else if (roll < 80) {
            output.println(state.getMessages().getBundle().get("recruit.archers.resource"));
            output.println(state.getMessages().getBundle().get("recruit.archers.resource.options"));
            handleArcherChoice(player, q, state);
        } else {
            output.println(state.getMessages().getBundle().get("recruit.archers.refuse"));
            output.println(state.getMessages().getBundle().get("recruit.archers.refuse.options"));
            handleArcherRefusalChoice(player, q, state);
        }
    }

    private void handleArcherChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output.print("Choose: ");
        String input = state.getGameServices().getInput().nextLine();
        switch (input) {
            case "1" -> output.println(state.getMessages().getBundle().get("recruit.archers.promise"));
            case "2" -> {
                if (player.payMoney(30, state)) {
                    output.println("💰 " + state.getMessages().getBundle().get("recruit.archers.gold.success"));
                    q.setArchersRecruited(true);
                } else {
                    output.println(state.getMessages().getBundle().get("recruit.archers.gold.fail"));
                }
            }
            case "3" -> {
                if (roll(state) < 40) {
                    output.println(state.getMessages().getBundle().get("recruit.archers.threat.success"));
                    q.setArchersRecruited(true);
                } else {
                    output.println(state.getMessages().getBundle().get("recruit.archers.threat.fail"));
                }
            }
            default -> output.println(state.getMessages().getBundle().get("recruit.archers.ignore"));
        }
    }

    private void handleArcherRefusalChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output.print("Choose: ");
        String input = state.getGameServices().getInput().nextLine();
        if (roll(state) < 50 && (input.equals("1") || input.equals("2"))) {
            output.println(state.getMessages().getBundle().get("recruit.archers.courage.success"));
            q.setArchersRecruited(true);
        } else {
            output.println(state.getMessages().getBundle().get("recruit.archers.courage.fail"));
        }
    }

    // === MAGES ===
    public void talkToTheMages(Player player, GameState state) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) {
            return;
        }
        q.setMagesTriedToRecruit(true);
        if (q.isMagesRecruited()) {
            output.println("🪄 " + state.getMessages().getBundle().get("recruit.mages.already"));
            return;
        }

        output.println();
        output.println("🔮 " + state.getMessages().getBundle().get("recruit.mages.enter"));

        int roll = roll(state);
        if (roll < 30) {
            output.println(state.getMessages().getBundle().get("recruit.mages.prophecy"));
            q.setMagesRecruited(true);
        } else if (roll < 75) {
            output.println(state.getMessages().getBundle().get("recruit.mages.price"));
            output.println(state.getMessages().getBundle().get("recruit.mages.price.options"));
            handleMageChoice(player, q, state);
        } else {
            output.println(state.getMessages().getBundle().get("recruit.mages.refuse"));
            output.println(state.getMessages().getBundle().get("recruit.mages.refuse.options"));
            handleMageRefusalChoice(player, q, state);
        }
    }

    private void handleMageChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output.print("Choose: ");
        String input = state.getGameServices().getInput().nextLine();
        switch (input) {
            case "1" -> {
                if (player.payMoney(70, state)) {
                    output.println("💰 " + state.getMessages().getBundle().get("recruit.mages.gold.success"));
                    q.setMagesRecruited(true);
                } else {
                    output.println(state.getMessages().getBundle().get("recruit.mages.gold.fail"));
                }
            }
            case "2" -> {
                if (roll(state) < 50) {
                    output.println(state.getMessages().getBundle().get("recruit.mages.reason.success"));
                    q.setMagesRecruited(true);
                } else {
                    output.println(state.getMessages().getBundle().get("recruit.mages.reason.fail"));
                }
            }
            default -> output.println(state.getMessages().getBundle().get("recruit.mages.crystals"));
        }
    }

    private void handleMageRefusalChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output.print("Choose: ");
        String input = state.getGameServices().getInput().nextLine();
        if (roll(state) < 40 && (input.equals("1") || input.equals("2"))) {
            output.println(state.getMessages().getBundle().get("recruit.mages.convince.success"));
            q.setMagesRecruited(true);
        } else {
            output.println(state.getMessages().getBundle().get("recruit.mages.convince.fail"));
        }
    }

    private RiseOfTheGoblinThreat checkSafe(Player player) {
        if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && player.getCurMission().getMissionType().equals(Missions.ASSEMBLE_ARMY)) {
            return q;
        }
        return null;
    }
}
