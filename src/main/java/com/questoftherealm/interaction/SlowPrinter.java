package com.questoftherealm.interaction;

import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

import static com.questoftherealm.game.GameConstants.DELAY_MS;

public class SlowPrinter {
    private final GameState state;

    public SlowPrinter(GameState state) {
        this.state = state;
    }

    public void slowPrint(String text) {
        Output output = state.getGameServices().getOutput();
        for (char c : text.toCharArray()) {
            output.print(String.valueOf(c));
            output.flush();
            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        output.println();
    }
}
