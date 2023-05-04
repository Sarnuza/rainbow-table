package kry;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.BiFunction;

public class RainbowTable {

    private static final String MD5_ALGORITHM_NAME = "md5";
    private static final int CHAIN_LENGTH = 2000;

    private final MessageDigest md5 = MessageDigest.getInstance(MD5_ALGORITHM_NAME);
    private final BiFunction<BigInteger, Integer, String> reductionFunction = new ReductionFunction();

    private final String[] startWords = WordGenerator.generateWords().toArray(String[]::new);
    private final String[] endWords = new String[startWords.length];

    public RainbowTable() throws NoSuchAlgorithmException {
        for (int i = 0; i < startWords.length; i++) {
            String word = startWords[i];
            for (int o = 0; o < CHAIN_LENGTH; o++) {
                word = generateChainLink(word, o);
            }
            endWords[i] = word;
        }
    }

    /**
     * Gets the hash of the given word and applies the reduction function to the hashed word.
     *
     * @param word      the word to be hashed and reduced
     * @param linkIndex the index of the chain link
     * @return the reduced value of the words hash
     */
    private String generateChainLink(String word, int linkIndex) {
        BigInteger hash = getWordHash(word);
        return reductionFunction.apply(hash, linkIndex);
    }

    /**
     * Hashes the given word and returns it in an <b>unsigned</b> integer representation
     *
     * @param word the word to be hashed
     * @return the <b>unsigned</b> integer representation of the words hash
     */
    private BigInteger getWordHash(String word) {
        byte[] hash = md5.digest(word.getBytes());

        /* Javas BigInteger is a signed integer. If the first byte is negative the resulting BigInteger will be
         * negative as well. Simply padding all bytes with a byte of value 0 eliminates the sign.
         */
        if (hash[0] < 0) {
            byte[] paddedHash = new byte[hash.length + 1];
            System.arraycopy(hash, 0, paddedHash, 1, hash.length);
            hash = paddedHash;
        }

        return new BigInteger(hash);
    }

    /* ****************************************************************************************** */

    public String findPasswordByHash(BigInteger hash) {
        int chainIndex = findHashInChain(hash);
        if (chainIndex == -1) {
            System.out.println("Password not found in RainbowTable");
            return null;
        }

        return findClearTextInChain(chainIndex, hash);
    }

    /**
     * Attempts to find a given hash in a chain and returns the chains index. In case the hash value
     * appears in none of the chains -1 is returned.
     *
     * @param hash the hash value as an integer
     * @return If found returns the index of the chain the hash value was found in otherwise returns -1
     */
    private int findHashInChain(BigInteger hash) {
        for (int linkIndex = CHAIN_LENGTH - 1; linkIndex >= 0; linkIndex--) {
            // only apply reduction function for first link (hash is already given)
            String word = reductionFunction.apply(hash, linkIndex);
            // +1 to skip one link (was calculated above)
            for (int o = linkIndex + 1; o < CHAIN_LENGTH; o++) {
                word = generateChainLink(word, o);
            }

            int endWordIndex = indexOfEndWord(word);
            if (endWordIndex != -1) {
                return endWordIndex;
            }
        }

        return -1;
    }

    /**
     * Finds the index of a given word in the end words list. Returns -1 if the word was not found.
     *
     * @param endWord the word to be searched for
     * @return if found index of the word, otherwise -1
     */
    private int indexOfEndWord(String endWord) {
        for (int i = 0; i < endWords.length; i++) {
            if (endWords[i].equals(endWord)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Reconstructs the chain at the given index and returns the clear text for the given hash.
     *
     * @param chainIndex index of the chain to be reconstructed
     * @param hash       hash value of the clear text to be reconstructed
     *                   return the clear text for the given hash
     */
    private String findClearTextInChain(int chainIndex, BigInteger hash) {
        int linkIndex = 0;
        String password = startWords[chainIndex];
        BigInteger passwordHash = getWordHash(password);
        while (passwordHash.compareTo(hash) != 0) {
            password = reductionFunction.apply(passwordHash, linkIndex++);
            passwordHash = getWordHash(password);
        }

        return password;
    }
}
