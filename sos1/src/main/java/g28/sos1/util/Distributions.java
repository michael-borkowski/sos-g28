package g28.sos1.util;

import java.util.Random;

public class Distributions {
    private Distributions() {
    }

    private static Random random = MyRandom.getRandom();

    public static Distribution uniform(long from, long to) {
        return () -> {
            long l = Math.abs(random.nextLong());
            return (l % (to - from)) + from;
        };
    }
}
