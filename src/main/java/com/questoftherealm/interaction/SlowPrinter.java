package com.questoftherealm.interaction;

import com.questoftherealm.game.GameState;
import com.questoftherealm.game.interfaces.Output;

import static com.questoftherealm.game.GameConstants.DELAY_MS;

public class SlowPrinter {
    private GameState state;
    private Output output;

    public SlowPrinter(GameState state) {
        this.state = state;
        this.output = state.getGameServices().getOutput();
    }

    public void slowPrint(String text) {
        for (char c : text.toCharArray()) {
            output.print(String.valueOf(c));
            try {
                Thread.sleep(DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        output.println();
    }
}
