package kry;

import java.util.ArrayList;
import java.util.List;

public class WordGenerator {

    private static final int NUMBER_OF_WORDS_TO_GENERATE = 2000;

    private static final char[] ALPHABET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final int WORD_LENGTH = 7;

    public static List<String> generateWords() {
        List<String> words = new ArrayList<>(NUMBER_OF_WORDS_TO_GENERATE);
        generateWordRecursive(words, new char[WORD_LENGTH], 0);
        return words;
    }

    private static void generateWordRecursive(List<String> passwords, char[] word, int characterIndex) {
        if (passwords.size() >= NUMBER_OF_WORDS_TO_GENERATE) {
            return;
        }
        if (characterIndex == WORD_LENGTH) {
            passwords.add(String.valueOf(word));
            return;
        }

        for (char c : ALPHABET) {
            word[characterIndex] = c;
            generateWordRecursive(passwords, word, characterIndex + 1);
        }
    }
}
