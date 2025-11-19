package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinKing;
import com.questoftherealm.expeditions.quests.FinalBattle;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;


public class GoblinKingManager {
    private Output output;
    private GameState state;
    private SlowPrinter slowPrinter;

    public GoblinKingManager(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.slowPrinter = new SlowPrinter(state);
    }

    public void goblinKingdomFound(Player player, FinalBattle q) {
        slowPrinter.slowPrint("🌄 " + MessageBundle.get("goblinking.intro"));
        q.setBreached(true);

        output.println(MessageBundle.get("goblinking.choice.menu"));
        int choice;
        try {
            choice = state.getGameServices().getInput().nextInt();
        } catch (Exception e) {
            choice = state.getGameServices().getRandom().random().nextInt(1, 3);
        }
        if (choice == 1) {
            stealthInfiltration(q);
        } else {
            fullAssault(player, q);
        }
    }

    private void fullAssault(Player player, FinalBattle q) {
        slowPrinter.slowPrint("⚔️ " + MessageBundle.get("goblinking.assault.start"));
        slowPrinter.slowPrint("💥 " + MessageBundle.get("goblinking.assault.mid"));
        output.println("🔥 " + MessageBundle.get("goblinking.assault.fight"));
        startBossFight(player, q);
    }

    private void stealthInfiltration(FinalBattle q) {
        slowPrinter.slowPrint("🕶️ " + MessageBundle.get("goblinking.stealth.start"));
        pause();

        slowPrinter.slowPrint("💀 " + MessageBundle.get("goblinking.stealth.throne"));

        int subChoice;
        try {
            subChoice = state.getGameServices().getInput().nextInt();
        } catch (Exception e) {
            subChoice = state.getGameServices().getRandom().random().nextInt(1, 3);
        }

        if (subChoice == 1) {
            slowPrinter.slowPrint("⚡ " + MessageBundle.get("goblinking.stealth.attack"));
            q.setDefeated(true);
            slowPrinter.slowPrint("👑 " + MessageBundle.get("goblinking.stealth.success"));
        } else {
            slowPrinter.slowPrint("⏳ " + MessageBundle.get("goblinking.stealth.wait"));
            q.setDefeated(true);
            slowPrinter.slowPrint("✨ " + MessageBundle.get("goblinking.stealth.silent.success"));
        }
    }

    private void startBossFight(Player player, FinalBattle q) {
        Characters character = player.getPlayerCharacter();
        GoblinKing king = new GoblinKing();
        int round = 1;

        slowPrinter.slowPrint("👑 " + MessageBundle.get("goblinking.boss.intro"));

        while (!character.isDead() && !king.isDead()) {
            output.println("\n🔥 ROUND " + round++ + " 🔥");
            output.println("💚 " + MessageBundle.get("goblinking.status.player", character.getHealth(), character.getMana()));
            output.println("❤️ " + MessageBundle.get("goblinking.status.boss", king.getHealth()));
            slowPrinter.slowPrint("\n" + king.getName() + " " + MessageBundle.get("goblinking.boss.prepare"));

            int bossMove = state.getGameServices().getRandom().randomInt(5);
            switch (bossMove) {
                case 0 -> output.println("⚔️ " + MessageBundle.get("goblinking.boss.move.0"));
                case 1 -> output.println("🏃‍♂️ " + MessageBundle.get("goblinking.boss.move.1"));
                case 2 -> output.println("😈 " + MessageBundle.get("goblinking.boss.move.2"));
                case 3 -> output.println("💨 " + MessageBundle.get("goblinking.boss.move.3"));
                case 4 -> output.println("🌋 " + MessageBundle.get("goblinking.boss.move.4"));
            }

            output.println(MessageBundle.get("goblinking.player.menu"));
            output.print("> ");
            String input = state.getGameServices().getInput().nextLine();

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
                        if (state.getGameServices().getRandom().random().nextInt(100) < 60) {
                            output.println("💨 " + MessageBundle.get("goblinking.player.dodge.success"));
                        } else {
                            output.println("❌ " + MessageBundle.get("goblinking.player.dodge.fail"));
                            damageToPlayer = king.getBaseAttack() / 2;
                        }
                    } else {
                        output.println("⚠️ " + MessageBundle.get("goblinking.player.nomana"));
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 2 -> { // Block
                    output.println("🛡️ " + MessageBundle.get("goblinking.player.block"));
                    damageToPlayer = king.getBaseAttack() / 3;
                    if (state.getGameServices().getRandom().random().nextInt(100) < 30) {
                        output.println("💥 " + MessageBundle.get("goblinking.player.parry"));
                        damageToBoss = character.getAttack() / 2;
                    }
                }
                case 3 -> { // Counterattack
                    output.println("⚔️ " + MessageBundle.get("goblinking.player.counter.start"));
                    if (state.getGameServices().getRandom().random().nextInt(100) < 45) {
                        output.println("🔥 " + MessageBundle.get("goblinking.player.counter.success"));
                        damageToBoss = character.getAttack();
                    } else {
                        output.println("💀 " + MessageBundle.get("goblinking.player.counter.fail"));
                        damageToPlayer = king.getBaseAttack();
                    }
                }
                case 4 -> { // Hide
                    output.println("🏗️ " + MessageBundle.get("goblinking.player.hide"));
                    if (state.getGameServices().getRandom().random().nextInt(100) < 60) {
                        output.println("🎯 " + MessageBundle.get("goblinking.player.hide.success"));
                        damageToBoss = (int) (character.getAttack() * 1.3);
                    } else {
                        output.println("💀 " + MessageBundle.get("goblinking.player.hide.fail"));
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                case 5 -> { // Potion
                    try {
                        String item = state.getGameServices().getInput().nextLine();
                        Item i = ItemRegistry.getItem(item);
                        player.useItem(i);
                    } catch (Exception e) {
                        output.println("⚠️ " + MessageBundle.get("goblinking.player.itemfail"));
                    }
                    output.println("💥 " + MessageBundle.get("goblinking.player.attack"));
                    if (state.getGameServices().getRandom().random().nextInt(100) < 50) {
                        damageToBoss = (int) (character.getAttack() * 1.5);
                        output.println("🔥 " + MessageBundle.get("goblinking.player.attack.success"));
                    } else {
                        output.println("😖 " + MessageBundle.get("goblinking.player.attack.fail"));
                        damageToPlayer = (int) (king.getBaseAttack() * 1.2);
                    }
                }
                default -> {
                    output.println("😨 " + MessageBundle.get("goblinking.player.hesitate"));
                    damageToPlayer = king.getBaseAttack();
                }
            }

            if (bossMove == 4 && state.getGameServices().getRandom().random().nextInt(100) < 60) {
                output.println("🌪️ " + MessageBundle.get("goblinking.boss.super"));
                damageToPlayer += (int) (king.getBaseAttack() * 1.5);
            }

            if ((double) king.getHealth() / CharacterConstants.GoblinKing_HEALTH <= 0.2 && state.getGameServices().getRandom().random().nextInt(100) < 40) {
                output.println("💢 " + MessageBundle.get("goblinking.boss.enrage"));
                king.setAttack((int) (king.getBaseAttack() * 1.3));
            }

            if (damageToBoss > 0) {
                king.takeDamage(damageToBoss,state);
                output.println("💥 " + MessageBundle.get("goblinking.damage.boss", damageToBoss));
            }
            if (damageToPlayer > 0) {
                character.takeDamage(damageToPlayer,state);
                output.println("😖 " + MessageBundle.get("goblinking.damage.player", damageToPlayer));
            }

            if (!king.isDead() && !character.isDead()) pause();
        }

        if (king.isDead()) {
            slowPrinter.slowPrint("⚔️ " + MessageBundle.get("goblinking.boss.death"));
            q.setDefeated(true);
        } else {
            slowPrinter.slowPrint("💀 " + MessageBundle.get("goblinking.boss.playerdeath"));
            character.setHealth(0);
        }
    }

    private void pause() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {
        }
    }
}
