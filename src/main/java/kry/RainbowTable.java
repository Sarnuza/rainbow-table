package kry;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RainbowTable {

    private static final String MD5_ALGORITHM_NAME = "md5";
    private static final int CHAIN_LENGTH = 2000;

    private final MessageDigest md5 = MessageDigest.getInstance(MD5_ALGORITHM_NAME);
    private final ReductionFunction reductionFunction = new ReductionFunction();

    private final String[] startPasswords = PasswordGenerator.generatePasswords().toArray(String[]::new);
    private final String[] endPasswords = new String[startPasswords.length];

    public RainbowTable() throws NoSuchAlgorithmException {
        for (int i = 0; i < startPasswords.length; i++) {
            String word = startPasswords[i];
            for (int o = 0; o < CHAIN_LENGTH; o++) {
                word = hashAndApplyReductionFunction(word, o);
            }
            endPasswords[i] = word;
        }
    }

    // Gets hash of string then applies the reduction function to it
    private String hashAndApplyReductionFunction(String word, int linkIndex) {
        BigInteger hash = getHash(word);
        return reductionFunction.apply(hash, linkIndex);
    }


    //get hash in biginteger by string
    private BigInteger getHash(String word) {
        byte[] digest = md5.digest(word.getBytes());
        return new BigInteger(1, digest);
    }

    public String findPasswordByHash(BigInteger hash) {
        int chainIndex = findHashInChain(hash);
        if (chainIndex == -1) {
            System.out.println("Password not found in RainbowTable");
            return null;
        }

        return findClearTextInChain(chainIndex, hash);
    }

    // find a hash in the chain, return index of the chain if found, -1 otherwise
    private int findHashInChain(BigInteger hash) {
        for (int linkIndex = CHAIN_LENGTH - 1; linkIndex >= 0; linkIndex--) {
            String word = reductionFunction.apply(hash, linkIndex);
            for (int o = linkIndex + 1; o < CHAIN_LENGTH; o++) {
                word = hashAndApplyReductionFunction(word, o);
            }

            int endWordIndex = -1;

            for (int i = 0; i < endPasswords.length; i++) {
                if (endPasswords[i].equals(word)) {
                    endWordIndex = i;
                    break;
                }
            }

            if (endWordIndex != -1) {
                return endWordIndex;
            }
        }

        return -1;
    }

    // find clear text in the chain by index of the chain and hash
    private String findClearTextInChain(int chainIndex, BigInteger hash) {
        int linkIndex = 0;
        String password = startPasswords[chainIndex];
        BigInteger passwordHash = getHash(password);
        while (passwordHash.compareTo(hash) != 0) {
            password = reductionFunction.apply(passwordHash, linkIndex++);
            passwordHash = getHash(password);
        }

        return password;
    }
}
