package g28.sos1.util;

import java.util.Random;

public class MyRandom {
    private MyRandom() {}

    public static Random getRandom() {
        return new Random();
    }
}
