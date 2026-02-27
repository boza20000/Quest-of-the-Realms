package com.questoftherealm.interaction;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.characters.playerCharacters.CharacterConstants;
import com.questoftherealm.characters.playerCharacters.Characters;
import com.questoftherealm.enemyEntities.bosses.GoblinKing;
import com.questoftherealm.expeditions.quest.quests.FinalBattle;
import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;
import com.questoftherealm.items.Item;
import com.questoftherealm.items.ItemRegistry;
import com.questoftherealm.localization.MessageBundle;


public class GoblinKingManager {
    private final Output output;
    private final GameState state;
    private final SlowPrinter slowPrinter;
    private final ItemRegistry itemRegistry;
    private final MessageBundle bundle;

    public GoblinKingManager(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
        this.slowPrinter = new SlowPrinter(state);
        this.itemRegistry = state.getItemRegistry();
        this.bundle = state.getMessages().getBundle();
    }

    public void goblinKingdomFound(Player player, FinalBattle q) {
        printGoblinBaseIntro(q);
        int choice = getChoice();
        if (choice == 1) {
            stealthInfiltration(q);
        } else {
            fullAssault(player, q);
        }
    }

    private int getChoice() {
        try {
            return state.getGameServices().getInput().nextInt();
        } catch (Exception e) {
            return state.getGameServices().getRandom().random().nextInt(1, 3);
        }
    }

    private void printGoblinBaseIntro(FinalBattle q) {
        slowPrinter.slowPrint("🌄 " + bundle.get("goblinking.intro"));
        q.setBreached(true);
        output.println(bundle.get("goblinking.choice.menu"));
    }

    private void fullAssault(Player player, FinalBattle q) {
        printAssaultStart();
        startBossFight(player, q);
    }

    private void printAssaultStart() {
        slowPrinter.slowPrint("⚔️ " + bundle.get("goblinking.assault.start"));
        slowPrinter.slowPrint("💥 " + bundle.get("goblinking.assault.mid"));
        output.println("🔥 " + bundle.get("goblinking.assault.fight"));
    }

    private void stealthInfiltration(FinalBattle q) {
        printStealthInfiltrationStart();
        if (getSubChoice() == 1) {
            printSilentAttack(q);
        } else {
            printSilentAttackWait(q);
        }
    }

    private void printSilentAttackWait(FinalBattle q) {
        slowPrinter.slowPrint("⏳ " + bundle.get("goblinking.stealth.wait"));
        q.setDefeated(true);
        slowPrinter.slowPrint("✨ " + bundle.get("goblinking.stealth.silent.success"));
    }

    private void printSilentAttack(FinalBattle q) {
        slowPrinter.slowPrint("⚡ " + bundle.get("goblinking.stealth.attack"));
        q.setDefeated(true);
        slowPrinter.slowPrint("👑 " + bundle.get("goblinking.stealth.success"));
    }

    private int getSubChoice() {
        try {
            return state.getGameServices().getInput().nextInt();
        } catch (Exception e) {
            return state.getGameServices().getRandom().random().nextInt(1, 3);
        }
    }

    private void printStealthInfiltrationStart() {
        slowPrinter.slowPrint("🕶️ " + bundle.get("goblinking.stealth.start"));
        pause();
        slowPrinter.slowPrint("💀 " + bundle.get("goblinking.stealth.throne"));
    }

    private void startBossFight(Player player, FinalBattle q) {
        Characters character = player.getPlayerCharacter();
        GoblinKing king = new GoblinKing(state);
        int round = 1;
        slowPrinter.slowPrint("👑 " + bundle.get("goblinking.boss.intro"));

        while (!character.isDead() && !king.isDead()) {
            output.println("\n🔥 ROUND " + round++ + " 🔥");
            printKingBattleIntro(character, king);

            int bossMove = getBossMove(state.getGameServices().getRandom().randomInt(5));
            printPlayerMenu();
            String input = state.getGameServices().getInput().nextLine();
            int choice = getPlayerChoice(input);

            switch (choice) {
                case 1 -> character.takeDamage(playerDodge(character, king), state, player);
                case 2 -> character.takeDamage(playerBlock(character, king), state, player);
                case 3 -> character.takeDamage(playerCounterAttack(character, king), state, player);
                case 4 -> character.takeDamage(playerHide(character, king), state, player);
                case 5 -> character.takeDamage(playerUseItem(player, king), state, player);
                default -> playerHesitates(king, player);
            }
            handleGoblinKingSuperPowers(bossMove, player, king);
            if (!king.isDead() && !character.isDead()) pause();
        }
        endBattle(king, character, q);
    }

    private void playerHesitates(GoblinKing king, Player player) {
        output.println("😨 " + bundle.get("goblinking.player.hesitate"));
        player.getPlayerCharacter().takeDamage(king.getBaseAttack(), state,player);
    }

    private void endBattle(GoblinKing king, Characters character, FinalBattle q) {
        if (king.isDead()) {
            slowPrinter.slowPrint("⚔️ " + bundle.get("goblinking.boss.death"));
            q.setDefeated(true);
        } else {
            slowPrinter.slowPrint("💀 " + bundle.get("goblinking.boss.playerdeath"));
            character.setHealth(0);
        }
    }

    private void handleGoblinKingSuperPowers(int bossMove, Player player, GoblinKing king) {
        if (bossMove == 4 && state.getGameServices().getRandom().random().nextInt(100) < 60) {
            output.println("🌪️ " + bundle.get("goblinking.boss.super"));
            player.getPlayerCharacter().takeDamage((int) (king.getBaseAttack() * 1.5), state,player);
        }

        if ((double) king.getHealth() / CharacterConstants.GoblinKing_HEALTH <= 0.2 && state.getGameServices().getRandom().random().nextInt(100) < 40) {
            output.println("💢 " + bundle.get("goblinking.boss.enrage"));
            king.setAttack((int) (king.getBaseAttack() * 1.3));
        }
    }

    private int playerUseItem(Player player, GoblinKing king) {
        try {
            String item = state.getGameServices().getInput().nextLine();
            Item i = itemRegistry.getItem(item);
            player.useItem(i);
            player.getPlayerCharacter().useMana(i.getMana());
        } catch (Exception e) {
            output.println("⚠️ " + bundle.get("goblinking.player.itemfail"));
        }
        output.println("💥 " + bundle.get("goblinking.player.attack"));
        if (state.getGameServices().getRandom().random().nextInt(100) < 50) {
            king.takeDamage((int) (player.getPlayerCharacter().getAttack() * 1.5), state);
            output.println("🔥 " + bundle.get("goblinking.player.attack.success"));
            return 0;
        } else {
            output.println("😖 " + bundle.get("goblinking.player.attack.fail"));
            return (int) (king.getBaseAttack() * 1.2);
        }

    }

    private int playerHide(Characters character, GoblinKing king) {
        output.println("🏗️ " + bundle.get("goblinking.player.hide"));
        if (state.getGameServices().getRandom().random().nextInt(100) < 60) {
            output.println("🎯 " + bundle.get("goblinking.player.hide.success"));
            king.takeDamage((int) (character.getAttack() * 1.3), state);
            return 0;
        } else {
            output.println("💀 " + bundle.get("goblinking.player.hide.fail"));
            return (int) (king.getBaseAttack() * 1.2);
        }
    }

    private int playerCounterAttack(Characters character, GoblinKing king) {
        output.println("⚔️ " + bundle.get("goblinking.player.counter.start"));
        if (state.getGameServices().getRandom().random().nextInt(100) < 45) {
            output.println("🔥 " + bundle.get("goblinking.player.counter.success"));
            king.takeDamage(character.getAttack(), state);
            return 0;
        } else {
            output.println("💀 " + bundle.get("goblinking.player.counter.fail"));
            return king.getBaseAttack();
        }
    }

    private int playerBlock(Characters character, GoblinKing king) {
        if (state.getGameServices().getRandom().random().nextInt(100) < 30) {
            output.println("💥 " + bundle.get("goblinking.player.parry"));
            return character.getAttack() / 2;
        }
        output.println("🛡️ " + bundle.get("goblinking.player.block"));
        return king.getBaseAttack() / 3;
    }

    private int playerDodge(Characters character, GoblinKing king) {
        if (character.getMana() >= 5) {
            character.setMana(character.getMana() - 5);
            if (state.getGameServices().getRandom().random().nextInt(100) < 60) {
                output.println("💨 " + bundle.get("goblinking.player.dodge.success"));
                return 0;
            } else {
                output.println("❌ " + bundle.get("goblinking.player.dodge.fail"));
                return king.getBaseAttack() / 2;
            }
        } else {
            output.println("⚠️ " + bundle.get("goblinking.player.nomana"));
            return king.getBaseAttack();
        }
    }

    private int getPlayerChoice(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void printPlayerMenu() {
        output.println(bundle.get("goblinking.player.menu"));
        output.print("> ");
        output.flush();
    }

    private int getBossMove(int choice) {
        switch (choice) {
            case 0 -> output.println("⚔️ " + bundle.get("goblinking.boss.move.0"));
            case 1 -> output.println("🏃‍♂️ " + bundle.get("goblinking.boss.move.1"));
            case 2 -> output.println("😈 " + bundle.get("goblinking.boss.move.2"));
            case 3 -> output.println("💨 " + bundle.get("goblinking.boss.move.3"));
            default -> output.println("🌋 " + bundle.get("goblinking.boss.move.4"));
        }
        return choice;
    }

    private void printKingBattleIntro(Characters character, GoblinKing king) {
        output.println("💚 " + bundle.get("goblinking.status.player", character.getHealth(), character.getMana()));
        output.println("❤️ " + bundle.get("goblinking.status.boss", king.getHealth()));
        slowPrinter.slowPrint("\n" + king.getName() + " " + bundle.get("goblinking.boss.prepare"));
    }

    private void pause() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {
        }
    }
}
