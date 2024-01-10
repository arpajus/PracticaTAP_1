package main.operations;

import java.math.BigInteger;

import main.Action;

public class CountWords extends Action {

    public CountWords(String id, double memory, String text) {
        super(id, memory, text);
    }

    @Override
    public void operation() {
        String text = getText();
        int wordCount = map(text);
        setResult(BigInteger.valueOf(wordCount));
    }

    private int map(String text) {
        String[] words = text.split("\\s+");
        return words.length;
    }

}
