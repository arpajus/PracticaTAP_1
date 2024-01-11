package main.main;

import java.util.List;

import main.Action;
import main.Controller;
import main.Invoker;
import main.decorator.InvokerCacheDecorator;
import main.operations.CountWords;
import main.operations.WordCount;
import main.policy.RoundRobinImproved;

public class MainMapReduce2 {
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

            // Mostrar resultados de las acciones
            System.out.println("Results of the actions: ");
            List<Action> results = controller.getActions();
            for (Action action : results) {
                if (action.getResult() != null) {
                    System.out.println("Result of the action " + action.getId() + ": " + action.getResult());
                } else if (action.getResultText() != null) {
                    System.out.println("Result of the action " + action.getId() + ": " + action.getResultText());
                }
            }
        } else {
            System.out.println("Not all actions could be assigned");
        }
    }
}