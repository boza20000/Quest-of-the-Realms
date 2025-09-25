package com.questoftherealm.interaction;

import java.util.Scanner;


public class Console {
    private static final Scanner scanner = new Scanner(System.in);
    public void clear() {
        System.out.println();
    }

    public void displayTitle() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║          Quest of the Realms          ║");
        System.out.println("║         Adventure Awaits You!         ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
    }

    public static void worldIntro() {
        System.out.println("🌍 And so, your journey begins...");
        System.out.println("You stand at the gates of the king’s castle, the weight of destiny upon your shoulders.");
        System.out.println("The banners of the four kingdoms ripple in the wind, their fate resting in your hands.");
        System.out.println("Whispers of a shadowy enemy spread across the land—an unknown force threatening to consume all.");
        System.out.println("\n(Press ENTER to continue...)");
        scanner.nextLine();
        System.out.println();
    }

}
