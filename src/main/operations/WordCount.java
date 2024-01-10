package main.operations;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import main.Action;

public class WordCount extends Action {

    public WordCount(String id, double memory, String text) {
        super(id, memory, text);
    }

    @Override
    public void operation() {
        String text=getText();
        Map<String, Integer> wordCountMap = map(text);
        setResultText(wordCountMap);
    }

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
