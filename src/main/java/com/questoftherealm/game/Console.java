package com.questoftherealm.game;

import java.io.IOException;

public class Console {

    public void clear() {
        int rows = 50; // approximate number of visible rows
        for (int i = 0; i < rows; i++) System.out.println(" ");
        // Move cursor back to top
        System.out.print("\033[H");
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


}
