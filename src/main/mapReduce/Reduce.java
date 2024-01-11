package main.mapReduce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reduce {
    
    public static Map<String,Integer> reduceWordCount(List<Map<String,Integer>> wordCountMaps){
        Map<String,Integer> result=new HashMap<>();

        for (Map<String, Integer> wordCountMap : wordCountMaps) {
            for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
                String word = entry.getKey();
                int count = entry.getValue();
                result.put(word, result.getOrDefault(word, 0) + count);
            }
        }

        return result;
    }

    public static int reduceCountWords(List<Integer> wordCounts) {
        int result = 0;

        for (int count : wordCounts) {
            result += count;
        }

        return result;
    }
}
