package com.questoftherealm.game;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomService {
    public Random random() {
        return ThreadLocalRandom.current();
    }
    public int randomInt(int range){
        return ThreadLocalRandom.current().nextInt(range);
    }
}

