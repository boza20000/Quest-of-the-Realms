package com.questoftherealm.interaction;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomManger {

    private static final ThreadLocal<Random> THREAD_RANDOM =
            ThreadLocal.withInitial(ThreadLocalRandom::current);

    private RandomManger() {}

    public static Random random() {
        return THREAD_RANDOM.get();
    }

}
