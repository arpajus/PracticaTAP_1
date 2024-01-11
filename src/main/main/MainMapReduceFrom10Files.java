package main.main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import main.mapReduce.Reduce;
import main.mapReduce.TextReader;
import main.operations.CountWords;
import main.operations.WordCount;

public class MainMapReduceFrom10Files {
    public static void main(String[] args) {

        // texts get readed and put into a list of strings
        List<String> texts = List.of(
                TextReader.readText("text/pg84.txt"),
                TextReader.readText("text/pg100.txt"),
                TextReader.readText("text/pg145.txt"),
                TextReader.readText("text/pg1342.txt"),
                TextReader.readText("text/pg1513.txt"),
                TextReader.readText("text/pg2641.txt"),
                TextReader.readText("text/pg2701.txt"),
                TextReader.readText("text/pg16389.txt"),
                TextReader.readText("text/pg37106.txt"),
                TextReader.readText("text/pg67979.txt"));

        System.out.println("----------------------");
        System.out.println("Text loaded");
        System.out.println("----------------------");

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

        System.out.println("----------------------");
        System.out.println("Text counted");
        System.out.println("Total word count result: " + totalCountWords);
        System.out.println("----------------------");

        boolean loop = true;
        Scanner sc = new Scanner(System.in);
        do {
            // Choose mode
            System.out.println("\n\n[0] - Exit program");
            System.out.println("[Any Word] - Search totalWordCounts");
            System.out.print("\nChoice: ");

            String input = sc.nextLine();
            if (input.equals("0")) {
                loop = false;
            } else {
                Integer totalCount = totalWordCounts.get(input);
                if (totalCount != null) {
                    System.out.println("Total word count result for " + input + ": " + totalCount);
                } else {
                    System.out.println("Word not found in the map.");
                }
            }
        } while (loop);
        sc.close();
    }
}
