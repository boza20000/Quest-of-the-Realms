package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinGeneral;
import com.questoftherealm.expeditions.quest.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.RandomService;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.localization.MessageBundle;
import com.questoftherealm.server.ServerLogger;


public class GoblinGeneralManager {
    private final GameState state;
    private final MessageBundle bundle;
    private final RandomService randomService;

    public GoblinGeneralManager(GameState state) {
        this.state = state;
        this.bundle = state.getMessages().getBundle();
        this.randomService = state.getGameServices().getRandom();
    }

    private Output output() {
        return state.getGameServices().getOutput();
    }

    private SlowPrinter slowPrinter() {
        return new SlowPrinter(state);
    }

    public void startFinalBattle(Player player, RiseOfTheGoblinThreat q, GameState state) {
        printBattleIntro();

        int playerArmyPower = calculatePlayerArmyPower(player, q);
        int enemyArmyPower = 120 + randomService.randomInt(60);

        printArmyStats(playerArmyPower, enemyArmyPower);
        simulateArmyBattle(player, playerArmyPower, enemyArmyPower, q, state);
    }

    private void printBattleIntro() {
        output().println("\n⚔️ " + bundle.get("goblinGeneral.battle.start"));
        output().println(bundle.get("goblinGeneral.battle.march"));
    }

    private void printArmyStats(int playerArmyPower, int enemyArmyPower) {
        output().println("🏇 " + bundle.get("goblinGeneral.battle.player.power") + playerArmyPower);
        output().println("👹 " + bundle.get("goblinGeneral.battle.enemy.power") + enemyArmyPower);
        output().println(bundle.get("goblinGeneral.battle.begin"));
    }

    private int calculatePlayerArmyPower(Player player, RiseOfTheGoblinThreat q) {
        int power = q.getArmyPower();
        power += player.getLevel() * 5;
        return power;
    }

    private void simulateArmyBattle(Player player, int playerPower, int enemyPower, RiseOfTheGoblinThreat q, GameState state) {
        double winChance = (double) playerPower / (playerPower + enemyPower);
        int roll = randomService.randomInt(100);

        printBattleSimulation(q, bundle);
        pause();

        if (roll < (winChance * 100)) {
            armyWinning(player, q, state);
        } else {
            armyLosing(player, bundle);
        }
    }

    private void armyWinning(Player player, RiseOfTheGoblinThreat q, GameState state) {
        printPlayerArmyWinning();
        duelGoblinGeneral(player, q, state);
    }

    private void printPlayerArmyWinning() {
        output().println("\n🏆 " + bundle.get("goblinGeneral.battle.victory"));
        output().println(bundle.get("goblinGeneral.battle.general.appears"));
    }

    private void armyLosing(Player player, MessageBundle bundle) {
        output().println("\n💀 " + bundle.get("goblinGeneral.battle.defeat"));
        player.getPlayerCharacter().setHealth(0);
        output().println(bundle.get("goblinGeneral.battle.death"));
    }

    private void printBattleSimulation(RiseOfTheGoblinThreat q, MessageBundle bundle) {
        printBattleSimulationIntro();

        if (q.isKnightsRecruited()) {
            pause();
            output().println(bundle.get("goblinGeneral.battle.knights"));
        }
        if (q.isMagesRecruited()) {
            pause();
            output().println(bundle.get("goblinGeneral.battle.mages"));
        }
        if (q.isArchersRecruited()) {
            pause();
            output().println(bundle.get("goblinGeneral.battle.archers"));
        }
    }

    private void printBattleSimulationIntro() {
        output().println("\n💥 " + bundle.get("goblinGeneral.battle.rage"));
        output().println(bundle.get("goblinGeneral.battle.scene"));
    }

    private void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            ServerLogger.get().warn("GoblinGeneralManager: Pause thread interrupted", e);
            Thread.currentThread().interrupt();
            output().println(bundle.get("goblinGeneral.error.sleep"));
        }
    }

    private void duelGoblinGeneral(Player player, RiseOfTheGoblinThreat q, GameState state) {
        goblinGeneralFight(player, state);
        if (!player.getPlayerCharacter().isDead()) {
            outcomePlayerKilledGeneral(q, state);
        } else {
            outcomePlayerKilled(player, q, state);
        }
    }

    private void outcomePlayerKilled(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output().println("\n💀 " + bundle.get("goblinGeneral.duel.death"));
        player.getPlayerCharacter().setHealth(0);
        output().println(bundle.get("goblinGeneral.duel.defeated"));
    }

    private void outcomePlayerKilledGeneral(RiseOfTheGoblinThreat q, GameState state) {
        output().println("\n🔥 " + bundle.get("goblinGeneral.duel.victory"));
        q.setDefeated(true);
        output().println("🎉 " + bundle.get("goblinGeneral.duel.safe"));
    }


    private void goblinGeneralFight(Player player, GameState state) {
        GoblinGeneral general = new GoblinGeneral(state);
        Characters character = player.getPlayerCharacter();
        output().println("\n👹 " + bundle.get("goblinGeneral.duel.intro"));
        int round = 1;

        while (!character.isDead() && !general.isDead()) {

            output().println("\n⚔️ " + bundle.get("goblinGeneral.duel.round") + " " + round++);
            printRoundInformation(state, general, character);
            makeBossMove(randomService.randomInt(3));

            printPlayerChoices();
            String input = state.getGameServices().getInput().nextLine();

            switch (input) {
                case "1" -> changePlayerHealth(player, playerChoice1(character, general));
                case "2" -> changePlayerHealth(player, playerChoice2(character, general));
                case "3" -> changePlayerHealth(player, playerChoice3(character, general));
                default -> changePlayerHealth(player, playerNoChoice(general));
            }

            double bossHpPercent = (double) general.getHealth() / CharacterConstants.GoblinGeneral_HEALTH;
            if (bossHpPercent <= 0.2 && randomService.randomInt(100) < 50) {
                activateBossSuper(general, player);
            }
            if (character.isDead() || general.isDead()) break;
            pause();
        }
    }

    private void activateBossSuper(GoblinGeneral general, Player player) {
        slowPrinter().slowPrint("\n⚡ " + bundle.get("goblinGeneral.duel.boss.enrage"));
        general.superMove(player, state);
        slowPrinter().slowPrint("\n⚡ " + bundle.get("goblinGeneral.duel.boss.heal"));
    }

    private void changePlayerHealth(Player player, int damageToPlayer) {
        if (damageToPlayer > 0) {
            player.getPlayerCharacter().takeDamage(damageToPlayer, state,player);
            output().println("😖 " + bundle.get("goblinGeneral.duel.take.damage") + damageToPlayer);
        }
    }

    private int playerNoChoice(GoblinGeneral general) {
        output().println(bundle.get("goblinGeneral.duel.hesitate"));
        return general.getBaseAttack();
    }

    private int playerChoice3(Characters character, GoblinGeneral general) {
        if (randomService.randomInt(100) < 40) {
            output().println("🔥 " + bundle.get("goblinGeneral.duel.counter.success"));
            general.takeDamage(character.getAttack(), state);
            return 0;
        } else {
            output().println("❌ " + bundle.get("goblinGeneral.duel.counter.fail"));
            return general.getBaseAttack();
        }
    }

    private int playerChoice2(Characters character, GoblinGeneral general) {
        if (randomService.randomInt(100) < 25) {
            output().println("💥 " + bundle.get("goblinGeneral.duel.parry"));
            general.takeDamage(character.getAttack() / 2, state);
            return 0;
        }
        output().println("🛡️ " + bundle.get("goblinGeneral.duel.block"));
        return general.getBaseAttack() / 3;
    }

    private int playerChoice1(Characters character, GoblinGeneral general) {
        synchronized (character) {
            if (character.getMana() >= 5) {
                character.setMana(character.getMana() - 5);
            } else {
                output().println("⚠️ " + bundle.get("goblinGeneral.duel.dodge.nomana"));
                return general.getBaseAttack();
            }
        }
        if (randomService.randomInt(100) < 60) {
            output().println("💨 " + bundle.get("goblinGeneral.duel.dodge.success"));
            return 0;
        } else {
            output().println("❌ " + bundle.get("goblinGeneral.duel.dodge.fail"));
            return general.getBaseAttack() / 2;
        }
    }

    private void printPlayerChoices() {
        output().println("\n" + bundle.get("goblinGeneral.duel.action"));
        output().println("1️⃣ " + bundle.get("goblinGeneral.duel.option.dodge"));
        output().println("2️⃣ " + bundle.get("goblinGeneral.duel.option.block"));
        output().println("3️⃣ " + bundle.get("goblinGeneral.duel.option.counter"));
        output().print(bundle.get("goblinGeneral.duel.choose"));
        output().flush();
    }

    private void makeBossMove(int bossMove) {
        switch (bossMove) {
            case 0 -> output().println("⚔️ " + bundle.get("goblinGeneral.duel.boss.swing"));
            case 1 -> output().println("🏃‍♂️ " + bundle.get("goblinGeneral.duel.boss.charge"));
            case 2 -> output().println("😈 " + bundle.get("goblinGeneral.duel.boss.feint"));
        }
    }

    private void printRoundInformation(GameState state, GoblinGeneral general, Characters character) {
        output().println("💚 " + bundle.get("goblinGeneral.duel.hp") + character.getHealth() +
                " | 🔵 " + bundle.get("goblinGeneral.duel.mana") + character.getMana());
        output().println("❤️ " + bundle.get("goblinGeneral.duel.boss.hp") + general.getHealth());
        slowPrinter().slowPrint("\n" + general.getName() + bundle.get("goblinGeneral.duel.raise.weapon"));
    }
}
