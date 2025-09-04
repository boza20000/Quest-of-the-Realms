package com.questoftherealm.game;

import com.questoftherealm.maps.Map;
import com.questoftherealm.player.Player;
import com.questoftherealm.player.PlayerTypes;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;

public class Game {
    private Player player;
    private Map gameMap;
    private Scanner scanner = new Scanner(System.in);
    private boolean gameOver = false;

    private void displayTitle() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║          Quest of the Realms          ║");
        System.out.println("║         Adventure Awaits You!         ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
    }

    private void prepareConsole() {
//console handle row,col amount
    }

    private void showIntro() {
        String story = """
                Welcome to Eldoria!
                
                For centuries, the four kingdoms — Humans, Orcs, Mages, and Elari —
                have lived in harmony. Recently, however, strange signs have appeared in the northern
                lands: villages abandoned, forests unusually quiet, and rumors of creatures
                moving in the shadows.
                
                You are a wanderer, sent on a mission to investigate the northern region of your kingdom.
                The council suspects that something is brewing, but no one knows what.
                Your task is clear: travel north, visit the villages, explore the area, report back,
                and uncover the truth behind these disturbances.
                
                The kingdom trusts your skill and judgment to report back with any findings.
                
                Your journey begins now. Pack your gear, steel your courage, and take your
                first step into the unknown. Stay vigilant—shadows hide more than they reveal,
                and danger could strike from anywhere. Trust no one,
                and remember some threats lurk where you least expect.
                
                Press Enter to begin your adventure...
                """;

        for (char c : story.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // properly handle interrupt
                System.out.println("Story printing interrupted!");
            }
        }
        // Wait for Enter
        try {
            System.in.read();
        } catch (Exception ignored) {}
    }

    private void giveOptions() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Quest of the Realms!\n" +
                "1. New Game\n" +
                "2. Load Game\n" +
                "Choose an option: ");



        System.out.println("Choose your character:");
        System.out.println("1. Warrior");
        System.out.println("2. Mage");
        System.out.println("3. Orc");
        System.out.println("4. Rogue");


        Player newPlayer = new Player("Admin", PlayerTypes.Mage);
        System.out.println(newPlayer.getPlayerCharacter());
        sc.close();
    }

    public void start() {
        giveOptions();

        prepareConsole();
        displayTitle();
        showIntro();

    }


}
