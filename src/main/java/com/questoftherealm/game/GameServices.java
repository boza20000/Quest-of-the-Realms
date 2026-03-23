package com.questoftherealm.game;

import com.questoftherealm.game.interfaces.Output;

public class GameServices {
    private final InputService input;
    private final RandomService random;
    private final Output output;

    public GameServices(Output type) {
        this.input = new InputService();
        this.random = new RandomService();
        this.output = type;
    }

    public InputService getInput() {
        return input;
    }

    public RandomService getRandom() {
        return random;
    }

    public Output getOutput() {
        return output;
    }
}
