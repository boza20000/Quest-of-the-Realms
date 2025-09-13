package com.questoftherealm.game;

import com.questoftherealm.game.Commands.Command;


public class Console {

    public void clear() {
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public void displayTitle() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║          Quest of the Realms          ║");
        System.out.println("║         Adventure Awaits You!         ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
    }

    public static void printErrorInline(String command) {
        System.out.print("\r" + " ".repeat(command.length() + 2) + "\r");
    }


}
