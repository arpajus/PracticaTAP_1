package main.main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.mapReduce.Reduce;
import main.operations.CountWords;
import main.operations.WordCount;

public class MainMapReduce {
    public static void main(String[] args) {
        List<String> texts = List.of(
                "Hola mundo",
                "Esto es un ejemplo",
                "MapReduce en Java es interesante",
                "Hola Java",
                "Ejemplo de MapReduce");

        // map of countWord
        List<CountWords> countWordsTasks = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            CountWords countWordsTask = new CountWords("CountWords" + i, 10, texts.get(i));
            countWordsTask.operation();
            countWordsTasks.add(countWordsTask);
        }

        // map of wordCount
        List<WordCount> wordCountTasks = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            WordCount wordCountTask = new WordCount("WordCount" + i, 0.0, texts.get(i));
            wordCountTask.operation();
            wordCountTasks.add(wordCountTask);
        }

        // reduce of countWord
        int totalCountWords = Reduce.reduceCountWords(countWordsTasks.stream()
                .map(CountWords::getResult)
                .map(BigInteger::intValue)
                .collect(Collectors.toList()));

        // reduce of wordCount
        Map<String, Integer> totalWordCounts = Reduce.reduceWordCount(wordCountTasks.stream()
                .map(WordCount::getResultText)
                .collect(Collectors.toList()));

        System.out.println("Total word count result:" + totalCountWords);
        System.out.println("Total word count result: " + totalWordCounts);
    }
}