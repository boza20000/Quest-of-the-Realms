package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinGeneral;
import com.questoftherealm.expeditions.quests.RiseOfTheGoblinThreat;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

import java.util.ResourceBundle;;

public class GoblinGeneralManager {
    private Output output;
    private SlowPrinter slowPrinter;
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public GoblinGeneralManager(GameState state){
        this.output = state.getGameServices().getOutput();
        this.slowPrinter = new SlowPrinter(state);
    }

    public void startFinalBattle(Player player, RiseOfTheGoblinThreat q, GameState state) {
        output.println("\n⚔️ " + messages.getString("goblinGeneral.battle.start"));
        output.println(messages.getString("goblinGeneral.battle.march"));

        int playerArmyPower = calculatePlayerArmyPower(player, q);
        int enemyArmyPower = 120 + state.getGameServices().getRandom().randomInt(60);

        output.println("🏇 " + messages.getString("goblinGeneral.battle.player.power") + playerArmyPower);
        output.println("👹 " + messages.getString("goblinGeneral.battle.enemy.power") + enemyArmyPower);
        output.println(messages.getString("goblinGeneral.battle.begin"));
        simulateArmyBattle(player, playerArmyPower, enemyArmyPower, q, state);
    }

    private int calculatePlayerArmyPower(Player player, RiseOfTheGoblinThreat q) {
        int power = q.getArmyPower();
        power += player.getLevel() * 5;
        return power;
    }

    private void simulateArmyBattle(Player player, int playerPower, int enemyPower, RiseOfTheGoblinThreat q, GameState state) {
        double winChance = (double) playerPower / (playerPower + enemyPower);
        int roll = state.getGameServices().getRandom().randomInt(100);

        output.println("\n💥 " + messages.getString("goblinGeneral.battle.rage"));
        output.println(messages.getString("goblinGeneral.battle.scene"));

        if (q.isKnightsRecruited()) {
            pause();
            output.println(messages.getString("goblinGeneral.battle.knights"));
        }
        if (q.isMagesRecruited()) {
            pause();
            output.println(messages.getString("goblinGeneral.battle.mages"));
        }
        if (q.isArchersRecruited()) {
            pause();
            output.println(messages.getString("goblinGeneral.battle.archers"));
        }

        pause();
        if (roll < (winChance * 100)) {
            output.println("\n🏆 " + messages.getString("goblinGeneral.battle.victory"));
            output.println(messages.getString("goblinGeneral.battle.general.appears"));
            duelGoblinGeneral(player, q, state);
        } else {
            output.println("\n💀 " + messages.getString("goblinGeneral.battle.defeat"));
            player.getPlayerCharacter().setHealth(0);
            output.println(messages.getString("goblinGeneral.battle.death"));
        }
    }

    private void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            output.println(messages.getString("goblinGeneral.error.sleep"));
        }
    }

    private void duelGoblinGeneral(Player player, RiseOfTheGoblinThreat q, GameState state) {
        goblinGeneralFight(player, state);
        if (!player.getPlayerCharacter().isDead()) {
            output.println("\n🔥 " + messages.getString("goblinGeneral.duel.victory"));
            q.setDefeated(true);
            output.println("🎉 " + messages.getString("goblinGeneral.duel.safe"));
        } else {
            output.println("\n💀 " + messages.getString("goblinGeneral.duel.death"));
            player.getPlayerCharacter().setHealth(0);
            output.println(messages.getString("goblinGeneral.duel.defeated"));
        }
    }

    private void goblinGeneralFight(Player player, GameState state) {
        GoblinGeneral general = new GoblinGeneral();
        Characters character = player.getPlayerCharacter();
        output.println("\n👹 " + messages.getString("goblinGeneral.duel.intro"));
        int round = 1;

        while (!character.isDead() && !general.isDead()) {
            output.println("\n⚔️ " + messages.getString("goblinGeneral.duel.round") + " " + round++);
            output.println("💚 " + messages.getString("goblinGeneral.duel.hp") + character.getHealth() +
                    " | 🔵 " + messages.getString("goblinGeneral.duel.mana") + character.getMana());
            output.println("❤️ " + messages.getString("goblinGeneral.duel.boss.hp") + general.getHealth());
            slowPrinter.slowPrint("\n" + general.getName() + messages.getString("goblinGeneral.duel.raise.weapon"));

            int bossMove = state.getGameServices().getRandom().randomInt(3);
            switch (bossMove) {
                case 0 -> output.println("⚔️ " + messages.getString("goblinGeneral.duel.boss.swing"));
                case 1 -> output.println("🏃‍♂️ " + messages.getString("goblinGeneral.duel.boss.charge"));
                case 2 -> output.println("😈 " + messages.getString("goblinGeneral.duel.boss.feint"));
            }

            output.println("\n" + messages.getString("goblinGeneral.duel.action"));
            output.println("1️⃣ " + messages.getString("goblinGeneral.duel.option.dodge"));
            output.println("2️⃣ " + messages.getString("goblinGeneral.duel.option.block"));
            output.println("3️⃣ " + messages.getString("goblinGeneral.duel.option.counter"));
            output.print(messages.getString("goblinGeneral.duel.choose"));
            String input = state.getGameServices().getInput().nextLine();

            int damageToBoss = 0;
            int damageToPlayer = 0;

            switch (input) {
                case "1" -> {
                    if (character.getMana() >= 5) {
                        character.setMana(character.getMana() - 5);
                        if (state.getGameServices().getRandom().randomInt(100) < 60)
                            output.println("💨 " + messages.getString("goblinGeneral.duel.dodge.success"));
                        else {
                            output.println("❌ " + messages.getString("goblinGeneral.duel.dodge.fail"));
                            damageToPlayer = general.getBaseAttack() / 2;
                        }
                    } else {
                        output.println("⚠️ " + messages.getString("goblinGeneral.duel.dodge.nomana"));
                        damageToPlayer = general.getBaseAttack();
                    }
                }
                case "2" -> {
                    output.println("🛡️ " + messages.getString("goblinGeneral.duel.block"));
                    damageToPlayer = general.getBaseAttack() / 3;
                    if (state.getGameServices().getRandom().randomInt(100) < 25) {
                        output.println("💥 " + messages.getString("goblinGeneral.duel.parry"));
                        damageToBoss = character.getAttack() / 2;
                    }
                }
                case "3" -> {
                    if (state.getGameServices().getRandom().randomInt(100) < 40) {
                        output.println("🔥 " + messages.getString("goblinGeneral.duel.counter.success"));
                        damageToBoss = character.getAttack();
                    } else {
                        output.println("❌ " + messages.getString("goblinGeneral.duel.counter.fail"));
                        damageToPlayer = general.getBaseAttack();
                    }
                }
                default -> {
                    output.println(messages.getString("goblinGeneral.duel.hesitate"));
                    damageToPlayer = general.getBaseAttack();
                }
            }

            if (damageToBoss > 0) {
                general.takeDamage(damageToBoss,state);
                output.println("💥 " + messages.getString("goblinGeneral.duel.deal.damage") + damageToBoss);
            }
            if (damageToPlayer > 0) {
                character.takeDamage(damageToPlayer,state);
                output.println("😖 " + messages.getString("goblinGeneral.duel.take.damage") + damageToPlayer);
            }

            double bossHpPercent = (double) general.getHealth() / CharacterConstants.GoblinGeneral_HEALTH;
            if (bossHpPercent <= 0.2 && state.getGameServices().getRandom().randomInt(100) < 50) {
                slowPrinter.slowPrint("\n⚡ " + messages.getString("goblinGeneral.duel.boss.enrage"));
                general.superMove(player,state);
                slowPrinter.slowPrint("\n⚡ " + messages.getString("goblinGeneral.duel.boss.heal"));
            }

            if (character.isDead() || general.isDead()) break;
            pause();
        }
    }
}
