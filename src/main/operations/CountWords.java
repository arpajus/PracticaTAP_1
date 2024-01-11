package main.operations;

import java.math.BigInteger;

import main.Action;

/**
 * Class representing a word counting operation.
 */
public class CountWords extends Action {

    /**
     * Constructs a CountWords instance with the specified ID, memory requirement, and text.
     *
     * @param id     The ID of the word counting operation.
     * @param memory The memory requirement for the operation.
     * @param text   The text on which the word counting operation is performed.
     */
    public CountWords(String id, double memory, String text) {
        super(id, memory, text);
    }

    /**
     * Performs the word counting operation on the specified text and sets the result.
     */
    @Override
    public void operation() {
        String text = getText();
        int wordCount = map(text);
        setResult(BigInteger.valueOf(wordCount));
    }

    /**
     * Splits the text into words and returns the word count.
     *
     * @param text The text to be counted.
     * @return The word count.
     */
    private int map(String text) {
        String[] words = text.split("\\s+");
        return words.length;
    }
}
