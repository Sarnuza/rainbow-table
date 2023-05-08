package kry;

import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {

    private static final int NUMBER_OF_PASSWORDS_TO_GENERATE = 2000;

    private static final char[] SYMBOLS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final int PASSWORD_LENGTH = 7;

    public static List<String> generatePasswords() {
        List<String> words = new ArrayList<>(NUMBER_OF_PASSWORDS_TO_GENERATE);
        generatePassword(words, new char[PASSWORD_LENGTH], 0);
        return words;
    }

    //Generate first PASSWORD_LENGTH passwords by SYMBOLS
    private static void generatePassword(List<String> passwords, char[] word, int characterIndex) {
        if (passwords.size() >= NUMBER_OF_PASSWORDS_TO_GENERATE) {
            return;
        }
        if (characterIndex == PASSWORD_LENGTH) {
            passwords.add(String.valueOf(word));
            return;
        }

        for (char c : SYMBOLS) {
            word[characterIndex] = c;
            generatePassword(passwords, word, characterIndex + 1);
        }
    }
}
