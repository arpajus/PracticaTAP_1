package main.operations;

import java.util.HashMap;
import java.util.Map;

import main.Action;

/**
 * Class representing a word count operation.
 */
public class WordCount extends Action {

    /**
     * Constructs a WordCount instance with the specified ID, memory requirement, and text.
     *
     * @param id     The ID of the word count operation.
     * @param memory The memory requirement for the operation.
     * @param text   The text on which the word count operation is performed.
     */
    public WordCount(String id, double memory, String text) {
        super(id, memory, text);
    }

    /**
     * Performs the word count operation on the specified text and sets the result.
     */
    @Override
    public void operation() {
        String text = getText();
        Map<String, Integer> wordCountMap = map(text);
        setResultText(wordCountMap);
    }

    /**
     * Splits the text into words, removes non-alphabetic characters, and counts the occurrences of each word.
     *
     * @param text The text to be processed for word counting.
     * @return A map containing words and their respective counts.
     */
    private Map<String, Integer> map(String text) {
        String[] words = text.split("\\s+");
        Map<String, Integer> wordCountMap = new HashMap<>();

        for (String word : words) {
            word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
            if (!word.isEmpty()) {
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }
        }

        return wordCountMap;
    }
}
