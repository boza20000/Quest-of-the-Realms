package com.questoftherealm.game;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomService {
    public Random random() {
        return ThreadLocalRandom.current();
    }

    public int randomInt(int range) {
        return ThreadLocalRandom.current().nextInt(range);
    }

    public int randomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    public double randomDouble(double range) {
        return ThreadLocalRandom.current().nextDouble(range);
    }

    public double randomDouble(double start, double end) {
        return ThreadLocalRandom.current().nextDouble(start, end);
    }


}

