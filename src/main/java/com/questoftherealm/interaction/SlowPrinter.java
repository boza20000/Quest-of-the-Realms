package com.questoftherealm.interaction;

import static com.questoftherealm.game.GameConstants.DELAY_MS;

public class SlowPrinter {
    public static void slowPrint(String text) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
}
