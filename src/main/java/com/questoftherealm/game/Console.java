package com.questoftherealm.game;

import com.questoftherealm.game.Commands.Command;


public class Console {

    public void clear() {
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public  void prepare() {
        //console handle row,col amount
    }

    public void displayTitle() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║          Quest of the Realms          ║");
        System.out.println("║         Adventure Awaits You!         ║");
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.println();
    }

    public static void printErrorInline(Command command) {
        System.out.print("\r" + " ".repeat(command.getName().length() + 2) + "\r");
    }


}
