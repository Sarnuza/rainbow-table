package kry;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        RainbowTable rainbowTable = new RainbowTable();

        BigInteger hash = new BigInteger("1d56a37fb6b08aa709fe90e12ca59e12", 16);
        System.out.println(rainbowTable.findPasswordByHash(hash));
    }
}
