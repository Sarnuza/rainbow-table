package kry;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RainbowTable {
    private static final int CHAIN_LENGTH = 2000;

    private final MessageDigest md5 = MessageDigest.getInstance("md5");
    private final ReductionFunction reductionFunction = new ReductionFunction();

    private final String[] startPasswords = PasswordGenerator.generatePasswords().toArray(String[]::new);
    private final String[] endPasswords = new String[startPasswords.length];

    public RainbowTable() throws NoSuchAlgorithmException {
        for (int i = 0; i < startPasswords.length; i++) {
            String password = startPasswords[i];
            for (int o = 0; o < CHAIN_LENGTH; o++) {
                password = hashAndReduce(password, o);
            }
            endPasswords[i] = password;
        }
    }

    // Gets hash of string then applies the reduction function to it
    private String hashAndReduce(String password, int linkIndex) {
        BigInteger hash = getHash(password);
        return reductionFunction.reduce(hash, linkIndex);
    }

    //get hash in biginteger by string
    private BigInteger getHash(String password) {
        byte[] digest = md5.digest(password.getBytes());
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
            String password = reductionFunction.reduce(hash, linkIndex);
            for (int o = linkIndex + 1; o < CHAIN_LENGTH; o++) {
                password = hashAndReduce(password, o);
            }

            int endPasswordIndex = -1;

            for (int i = 0; i < endPasswords.length; i++) {
                if (endPasswords[i].equals(password)) {
                    endPasswordIndex = i;
                    break;
                }
            }

            if (endPasswordIndex != -1) {
                return endPasswordIndex;
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
            password = reductionFunction.reduce(passwordHash, linkIndex++);
            passwordHash = getHash(password);
        }

        return password;
    }
}
