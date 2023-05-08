package kry;

import java.math.BigInteger;

public class ReductionFunction {

    private static final char[] SYMBOLS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final BigInteger SYMBOLS_LENGTH = BigInteger.valueOf(SYMBOLS.length);
    private static final int PASSWORD_LENGTH = 7;

    // applies reduction function to hash
    public String reduce(BigInteger hash, Integer step) {
        BigInteger _hash = hash.add(BigInteger.valueOf(step));
        int[] remainder = new int[PASSWORD_LENGTH];
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            remainder[i] = _hash.remainder(SYMBOLS_LENGTH).intValueExact();
            _hash = _hash.divide(SYMBOLS_LENGTH);
        }

        char[] password = new char[PASSWORD_LENGTH];
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password[i] = SYMBOLS[remainder[PASSWORD_LENGTH - 1 - i]];
        }
        return String.valueOf(password);
    }
}
