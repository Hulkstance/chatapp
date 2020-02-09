package server;

import java.util.Random;

/**
 * This class uses a thread-safe singleton design pattern.
 */
public class Util {

    private static final Util instance = new Util();

    private Util() {
    }

    public static Util getInstance() {
        return instance;
    }

    /**
     * Generates a random string.
     *
     * @param length the length of the string.
     * @return the generated string.
     */
    public String getRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
