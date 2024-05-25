package com.sebastian.sockets.math;

import java.util.Random;

public class RandomMath {
    private static final Random RANDOM = new Random();

    public static float getRandomFloat(float min, float max) {
        return min + RANDOM.nextFloat() * (max - min);
    }
}
