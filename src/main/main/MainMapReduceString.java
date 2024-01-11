package main.main;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.Action;
import main.Controller;
import main.Invoker;
import main.decorator.InvokerCacheDecorator;
import main.mapReduce.Reduce;
import main.operations.CountWords;
import main.operations.WordCount;
import main.policy.RoundRobinImproved;

public class MainMapReduceString {
    public static void main(String[] args) {
        Controller controller = Controller.getInstance();
        RoundRobinImproved roundRobinImproved = new RoundRobinImproved();
        controller.setPolicy(roundRobinImproved);

        Invoker iv1 = new InvokerCacheDecorator(new Invoker(1000, "1"));
        controller.addInvoker(iv1);
        iv1.addObserver(controller);

        Invoker iv2 = new Invoker(2000, "2");
        controller.addInvoker(iv2);
        iv2.addObserver(controller);
        String text1 = "Hola mundo Esto es un ejemplo MapReduce en Java es interesante Hola Java Ejemplo de MapReduce";

        CountWords countWordsTask = new CountWords("CountWords", 10, text1);
        WordCount wordCountTask = new WordCount("WordCount", 10, text1);

        controller.addAction(countWordsTask);
        controller.addAction(wordCountTask);

        System.out.println("----------------------");
        System.out.println("----------------------");

        if (controller.distributeActions()) {
            System.out.println("Actions distributed");

            System.out.println("The actual memory of iv1 is " + controller.getInvokers().get(0).getTotalMemory());
            System.out.println("The actual memory of iv2 is " + controller.getInvokers().get(1).getTotalMemory());

            controller.executeAssignedActions();
            System.out.println("Actions executed");

            controller.printMetrics();

            System.out.println("The actual memory of iv1 is " + controller.getInvokers().get(0).getTotalMemory());
            System.out.println("The actual memory of iv2 is " + controller.getInvokers().get(1).getTotalMemory());

            System.out.println("-----------------");
            controller.analyzeExecutionTime(controller.getMetrics());
            controller.analyzeInvokerMemory(controller.getMetrics());
            System.out.println("-------------");
            controller.analyzeExecutionTimeBis(controller.getMetrics());

            // Obtain results of actions
            System.out.println("Results of the actions: ");
            List<Action> results = controller.getActions();

            // filter actions wordCount and count
            List<WordCount> wordCountResults = results.stream()
                    .filter(action -> action instanceof WordCount)
                    .map(action -> (WordCount) action)
                    .collect(Collectors.toList());

            List<CountWords> countWordsResults = results.stream()
                    .filter(action -> action instanceof CountWords)
                    .map(action -> (CountWords) action)
                    .collect(Collectors.toList());

            // reduce wordCount
            Map<String, Integer> consolidatedWordCountResult = Reduce.reduceWordCount(wordCountResults.stream()
                    .map(WordCount::getResultText)
                    .collect(Collectors.toList()));

            // reduce countWord
            BigInteger consolidatedCountWordsResult = Reduce.reduceCountWords(countWordsResults.stream()
                    .map(CountWords::getResult)
                    .collect(Collectors.toList()));

            for (Action action : results) {
                if (action.getResult() != null) {
                    System.out.println("Result of the action " + action.getId() + ": " + consolidatedCountWordsResult);
                } else if (action.getResultText() != null) {
                    System.out.println("Result of the action " + action.getId() + ": " + consolidatedWordCountResult);
                }
            }
        } else {
            System.out.println("Not all actions could be assigned");
        }
    }
}
