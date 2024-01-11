package main.mapReduce;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the Reduce phase in a MapReduce operation.
 */
public class Reduce {

    /**
     * Reduces word counts from multiple maps into a single word count map.
     *
     * @param wordCountMaps The list of word count maps to be reduced.
     * @return The reduced word count map.
     */
    public static Map<String, Integer> reduceWordCount(List<Map<String, Integer>> wordCountMaps) {
        Map<String, Integer> result = new HashMap<>();
        for (Map<String, Integer> wordCountMap : wordCountMaps) {
            for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();
                result.put(word, result.getOrDefault(word, 0) + count);
            }
        }

        return result;
    }

    /**
     * Reduces word counts from a list into a single total word count.
     *
     * @param wordCounts The list of word counts to be reduced.
     * @return The reduced total word count.
     */
    public static BigInteger reduceCountWords(List<BigInteger> wordCounts) {
        BigInteger result = BigInteger.ZERO;

        for (BigInteger count : wordCounts) {
            result = result.add(count);
        }
        return result;
    }
}

