package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.expeditions.missions.Missions;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;


public class RecruitmentManager {
    private GameState state;

    public RecruitmentManager(GameState state) {
        this.state = state;
    }

    private int roll(GameState state) {
        return state.getGameServices().getRandom().random().nextInt(100);
    }
    
    public Output output(){
        return state.getGameServices().getOutput();
    }

    // ========== UTILITY FUNCTIONS ==========
    
    private String promptAndGetInput(String promptKey) {
        output().print(state.getMessages().getBundle().get(promptKey));
        output().flush();
        return state.getGameServices().getInput().nextLine().trim();
    }

    private void printMessage(String messageKey) {
        output().println(state.getMessages().getBundle().get(messageKey));
    }

    private void printWithEmoji(String emoji, String messageKey) {
        output().println(emoji + " " + state.getMessages().getBundle().get(messageKey));
    }

    private void checkAlreadyRecruited(boolean isRecruited, String emoji, String messageKey) {
        if (isRecruited) {
            printWithEmoji(emoji, messageKey);
        }
    }

    private boolean attemptPayment(Player player, int cost, String successKey, String failKey) {
        if (player.payMoney(cost, state)) {
            printWithEmoji("💰", successKey);
            return true;
        } else {
            printMessage(failKey);
            return false;
        }
    }

    private boolean handleRollBasedSuccess(int threshold, String successKey, String failKey) {
        if (roll(state) < threshold) {
            printMessage(successKey);
            return true;
        } else {
            printMessage(failKey);
            return false;
        }
    }

    private String displayChoicesAndGetInput(String... choiceKeys) {
        output().println();
        for (String choiceKey : choiceKeys) {
            output().println(state.getMessages().getBundle().get(choiceKey));
        }
        return promptAndGetInput("prompt.arrow");
    }

    // === KNIGHTS ===
    public void talkToTheKnights(Player player, GameState state) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) return;
        
        q.setKnightsTriedToRecruit(true);
        
        if (q.isKnightsRecruited()) {
            printWithEmoji("⚔️", "recruit.knights.already");
            return;
        }

        printWithEmoji("🏰", "recruit.knights.enter");
        printMessage("recruit.knights.commander.greet");

        while (!q.isKnightsRecruited()) {
            String choice = displayChoicesAndGetInput(
                "1️⃣ recruit.knights.choice.1",
                "2️⃣ recruit.knights.choice.2",
                "3️⃣ recruit.knights.choice.3"
            );

            switch (choice) {
                case "1" -> persuasionBranch(player, q, state);
                case "2" -> intimidationBranch(player, q, state);
                case "3" -> {
                    printMessage("recruit.knights.leave");
                    return;
                }
                default -> printMessage("recruit.knights.invalid");
            }
        }
    }

    private void persuasionBranch(Player player, RiseOfTheGoblinThreat q, GameState state) {
        printMessage("recruit.knights.persuade.start");
        
        if (handleRollBasedSuccess(50, "recruit.knights.persuade.success", "recruit.knights.persuade.fail")) {
            q.setKnightsRecruited(true);
            return;
        }

        printMessage("recruit.knights.persuade.options");
        String choice = promptAndGetInput("prompt.arrow");
        
        switch (choice) {
            case "1" -> {
                if (attemptPayment(player, 50, "recruit.knights.pay.success", "recruit.knights.pay.fail")) {
                    q.setKnightsRecruited(true);
                }
            }
            case "2" -> {
                if (handleRollBasedSuccess(40, "recruit.knights.persuade.reason.success", "recruit.knights.persuade.reason.fail")) {
                    q.setKnightsRecruited(true);
                }
            }
            case "3" -> printMessage("recruit.knights.persuade.refuse");
        }
    }

    private void intimidationBranch(Player player, RiseOfTheGoblinThreat q, GameState state) {
        printMessage("recruit.knights.intimidate.start");
        
        if (handleRollBasedSuccess(45, "recruit.knights.intimidate.success", "recruit.knights.intimidate.fail")) {
            q.setKnightsRecruited(true);
            return;
        }

        printMessage("recruit.knights.intimidate.options");
        String choice = promptAndGetInput("prompt.arrow");
        
        switch (choice) {
            case "1" -> persuasionBranch(player, q, state);
            case "2" -> {
                if (handleRollBasedSuccess(30, "recruit.knights.intimidate.double.success", "recruit.knights.intimidate.double.fail")) {
                    q.setKnightsRecruited(true);
                }
            }
            case "3" -> printMessage("recruit.knights.intimidate.leave");
        }
    }

    // === ARCHERS ===
    public void talkToTheArchers(Player player, GameState state) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) return;
        
        q.setArchersTriedToRecruit(true);
        
        if (q.isArchersRecruited()) {
            printWithEmoji("🏹", "recruit.archers.already");
            return;
        }

        printWithEmoji("🌲", "recruit.archers.find");

        int rollValue = roll(state);
        if (rollValue < 35) {
            printMessage("recruit.archers.eager");
            q.setArchersRecruited(true);
        } else if (rollValue < 80) {
            printMessage("recruit.archers.resource");
            printMessage("recruit.archers.resource.options");
            handleArcherChoice(player, q, state);
        } else {
            printMessage("recruit.archers.refuse");
            printMessage("recruit.archers.refuse.options");
            handleArcherRefusalChoice(player, q, state);
        }
    }

    private void handleArcherChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        String input = promptAndGetInput("prompt.choose");
        switch (input) {
            case "1" -> printMessage("recruit.archers.promise");
            case "2" -> {
                if (attemptPayment(player, 30, "recruit.archers.gold.success", "recruit.archers.gold.fail")) {
                    q.setArchersRecruited(true);
                }
            }
            case "3" -> {
                if (handleRollBasedSuccess(40, "recruit.archers.threat.success", "recruit.archers.threat.fail")) {
                    q.setArchersRecruited(true);
                }
            }
            default -> printMessage("recruit.archers.ignore");
        }
    }

    private void handleArcherRefusalChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        String input = promptAndGetInput("prompt.choose");
        if (roll(state) < 50 && (input.equals("1") || input.equals("2"))) {
            printMessage("recruit.archers.courage.success");
            q.setArchersRecruited(true);
        } else {
            printMessage("recruit.archers.courage.fail");
        }
    }

    // === MAGES ===
    public void talkToTheMages(Player player, GameState state) {
        RiseOfTheGoblinThreat q = checkSafe(player);
        if (q == null) return;
        
        q.setMagesTriedToRecruit(true);
        
        if (q.isMagesRecruited()) {
            printWithEmoji("🪄", "recruit.mages.already");
            return;
        }

        printWithEmoji("🔮", "recruit.mages.enter");

        int rollValue = roll(state);
        if (rollValue < 30) {
            printMessage("recruit.mages.prophecy");
            q.setMagesRecruited(true);
        } else if (rollValue < 75) {
            printMessage("recruit.mages.price");
            printMessage("recruit.mages.price.options");
            handleMageChoice(player, q, state);
        } else {
            printMessage("recruit.mages.refuse");
            printMessage("recruit.mages.refuse.options");
            handleMageRefusalChoice(player, q, state);
        }
    }

    private void handleMageChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        String input = promptAndGetInput("prompt.choose");
        switch (input) {
            case "1" -> {
                if (attemptPayment(player, 70, "recruit.mages.gold.success", "recruit.mages.gold.fail")) {
                    q.setMagesRecruited(true);
                }
            }
            case "2" -> {
                if (handleRollBasedSuccess(50, "recruit.mages.reason.success", "recruit.mages.reason.fail")) {
                    q.setMagesRecruited(true);
                }
            }
            default -> printMessage("recruit.mages.crystals");
        }
    }

    private void handleMageRefusalChoice(Player player, RiseOfTheGoblinThreat q, GameState state) {
        String input = promptAndGetInput("prompt.choose");
        if (roll(state) < 40 && (input.equals("1") || input.equals("2"))) {
            printMessage("recruit.mages.convince.success");
            q.setMagesRecruited(true);
        } else {
            printMessage("recruit.mages.convince.fail");
        }
    }

    private RiseOfTheGoblinThreat checkSafe(Player player) {
        if (player.getCurQuest() instanceof RiseOfTheGoblinThreat q && player.getCurMission().getMissionType().equals(Missions.ASSEMBLE_ARMY)) {
            return q;
        }
        return null;
    }
}



