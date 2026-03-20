package com.questoftherealm.game;

import com.questoftherealm.game.interfaces.Input;
import com.questoftherealm.game.interfaces.Output;

public class GameServices {
    private final Input input;
    private final RandomService random;
    private final Output output;

    public GameServices(Output output, Input input) {
        this(output, input, new RandomService());
    }

    public GameServices(Output output, Input input, RandomService random) {
        this.input = input;
        this.random = random;
        this.output = output;
    }

    public Input getInput() {
        return input;
    }

    public RandomService getRandom() {
        return random;
    }

    public Output getOutput() {
        return output;
    }
}
