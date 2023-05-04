package kry;

import java.math.BigInteger;
import java.util.function.BiFunction;

public class ReductionFunction implements BiFunction<BigInteger, Integer, String> {

    private static final char[] ALPHABET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final BigInteger ALPHABET_LENGTH = BigInteger.valueOf(ALPHABET.length);
    private static final int WORD_LENGTH = 7;

    @Override
    public String apply(BigInteger hash, Integer step) {
        BigInteger hash_ = hash.add(BigInteger.valueOf(step));
        int[] remainder = new int[WORD_LENGTH];
        for (int i = 0; i < WORD_LENGTH; i++) {
            remainder[i] = hash_.remainder(ALPHABET_LENGTH).intValueExact();
            hash_ = hash_.divide(ALPHABET_LENGTH);
        }

        char[] word = new char[WORD_LENGTH];
        for (int i = 0; i < WORD_LENGTH; i++) {
            word[i] = ALPHABET[remainder[WORD_LENGTH - 1 - i]];
        }
        return String.valueOf(word);
    }
}
